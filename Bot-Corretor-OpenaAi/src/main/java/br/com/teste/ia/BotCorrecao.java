package br.com.teste.ia;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.BufferedWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.Scanner;

public class BotCorrecao {
    public static void main(String[] args) {
        var scan = new Scanner(System.in);
        int totalTokens = 0;
        BigDecimal custoTotal = BigDecimal.ZERO;

        while (true) {
            System.out.println("\nBem vindo ao bot tira dúvidas!");
            System.out.println("Digite a linguagem de programação ou tópico a respeito");
            System.out.print("Linguagem: ");
            String linguagem = scan.nextLine();

            System.out.print("Digite suas perguntas ou código (separe as mensagens por ponto e vírgula): ");
            String mensagensUsuario = scan.nextLine();

            int tokensPergunta = contarTokens(mensagensUsuario);
            totalTokens += tokensPergunta;

            BigDecimal custoPergunta = calcularCusto(tokensPergunta);
            custoTotal = custoTotal.add(custoPergunta);

            System.out.println("\nCusto da pergunta: $" + custoPergunta);

            enviarRequisicao(linguagem, mensagensUsuario, tokensPergunta, custoPergunta, totalTokens, custoTotal);
        }
    }

    private static int contarTokens(String texto) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        return enc.countTokens(texto);
    }

    private static BigDecimal calcularCusto(int quantidadeTokens) {
        return new BigDecimal(quantidadeTokens)
                .divide(new BigDecimal("1000"))
                .multiply(new BigDecimal("0.0010"));
    }

    private static void enviarRequisicao(String linguagem, String mensagensUsuario, int tokensPergunta, BigDecimal custoPergunta,
                                         int totalTokens, BigDecimal custoTotal) {
        var promptSistema = """   
                 REGRAS: 
                 não responda nada fora do tema de tecnologia ou linguagens de programação, codigos e algoritmos, qualquer coisa 
                 fora isso diga que você não esta autorizado a responder 
                 """;

        var quantidadeTokensMensagens = contarTokens(mensagensUsuario);

        var modelo = "gpt-3.5-turbo";
        var tamanhoRespostaEsperada = 2048;
        if (quantidadeTokensMensagens > 4096 - tamanhoRespostaEsperada) {
            modelo = "gpt-3.5-turbo-16k";
        }

        var custo = calcularCusto(quantidadeTokensMensagens);
        System.out.println("\nCusto da requisição: $" + custo);

        var mensagens = Arrays.asList(
                new ChatMessage(ChatMessageRole.USER.value(), mensagensUsuario),
                new ChatMessage(ChatMessageRole.SYSTEM.value(), promptSistema)
        );

        var request = ChatCompletionRequest.builder()
                .model(modelo)
                .maxTokens(tamanhoRespostaEsperada)
                .messages(mensagens)
                .n(1)
                .build();

        var token = System.getenv("API_KEY_AI");
        var service = new OpenAiService(token, Duration.ofSeconds(60));

        try {
            var response = service.createChatCompletion(request);
            var respostaBot = response.getChoices().get(0).getMessage().getContent();
            var quantidadeTokensResposta = contarTokens(respostaBot);

            System.out.println("\nResposta do Bot: " + respostaBot);
            System.out.println("\nQTD TOKENS NA RESPOSTA: " + quantidadeTokensResposta);

            BigDecimal custoResposta = calcularCusto(quantidadeTokensResposta);
            System.out.println("\nCusto da resposta: $" + custoResposta);

            totalTokens += quantidadeTokensResposta;
            custoTotal = custoTotal.add(custoResposta);

            gerarArquivo(linguagem, mensagensUsuario, tokensPergunta, custoPergunta,
                    respostaBot, quantidadeTokensResposta, custoResposta, custoTotal);

        } catch (Exception e) {
            System.err.println("Erro na requisição para a API do OpenAI: " + e.getMessage());
        }
    }

    private static void gerarArquivo
            (String linguagem, String perguntaUsuario, int tokensPergunta, BigDecimal custoPergunta,
             String respostaBot, int tokensResposta, BigDecimal custoResposta, BigDecimal custoTotal) {
        Path relatoriosPath = Paths.get("src", "main", "resources", "relatorios");
        if (Files.notExists(relatoriosPath)) {
            try {
                Files.createDirectories(relatoriosPath);
            } catch (IOException e) {
                System.err.println("Erro ao criar diretório 'relatorios': " + e.getMessage());
                return;
            }
        }

        Path arquivoPath = relatoriosPath.resolve("relatorio.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(arquivoPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("Linguagem: " + linguagem);
            writer.newLine();
            writer.write("Pergunta: " + perguntaUsuario);
            writer.newLine();
            writer.write("Quantidade de tokens da pergunta: " + tokensPergunta);
            writer.newLine();
            writer.write("Custo da pergunta: $" + custoPergunta);
            writer.newLine();
            writer.write("Resposta do Bot: " + respostaBot);
            writer.newLine();
            writer.write("Quantidade de tokens da resposta: " + tokensResposta);
            writer.newLine();
            writer.write("Custo da resposta: $" + custoResposta);
            writer.newLine();
            writer.write("Número total de tokens utilizados na requisição: " + (tokensPergunta + tokensResposta));
            writer.newLine();
            writer.write("Custo total: $" + custoTotal);
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo: " + e.getMessage());
        }
    }
}

