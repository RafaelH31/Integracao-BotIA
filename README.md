#Documentação para BotCorrecao
Visão Geral
BotCorrecao é um programa Java que utiliza o modelo OpenAI GPT-3.5 Turbo para responder a perguntas relacionadas a linguagens de programação e tópicos de codificação. O programa interage com a API da OpenAI para gerar respostas com base na entrada do usuário. Além disso, ele mantém o controle do custo associado a cada consulta do usuário e registra as interações em um arquivo de texto.

Iniciando
Pré-requisitos
Kit de Desenvolvimento Java (JDK) 11 ou superior.
Chave de API da OpenAI (Definida como uma variável de ambiente com o nome API_KEY_AI).
Executando o Programa
Compile e execute o programa usando um ambiente de desenvolvimento Java ou executando o arquivo JAR compilado.
Insira a linguagem de programação ou tópico quando solicitado.
Insira perguntas ou trechos de código do usuário, separados por ponto e vírgula.
O programa calcula o custo da entrada e envia uma solicitação para a API da OpenAI.
A resposta da API é exibida, junto com seu custo e contagem de tokens.
O programa registra a interação em um arquivo de texto chamado relatorio.txt no diretório "src/main/resources/relatorios".
Estrutura do Código
O programa está organizado em vários métodos para melhorar a legibilidade e a manutenibilidade. Abaixo estão os componentes principais:

Método main
O ponto de entrada do programa.
Aceita a entrada do usuário para a linguagem de programação e as mensagens do usuário.
Itera indefinidamente, permitindo que o usuário faça várias perguntas.
Método contarTokens
Conta o número de tokens em um texto fornecido usando a biblioteca Encodings da OpenAI.
Método calcularCusto
Calcula o custo de um determinado número de tokens com base em um custo pré-definido por mil tokens.
Método enviarRequisicao
Envia uma solicitação para a API da OpenAI usando a entrada fornecida pelo usuário.
Determina o modelo GPT-3.5 Turbo apropriado com base na contagem de tokens.
Exibe o custo da solicitação à API e processa a resposta.
Método gerarArquivo
Cria e anexa informações sobre a interação do usuário a um arquivo de log.
Manipula operações de I/O de arquivo para armazenar detalhes, como linguagem, entrada do usuário e resposta da API.
Tratamento de Erros
O programa inclui tratamento básico de erros para gerenciar exceções que podem ocorrer durante solicitações à API ou operações de arquivo.
Quaisquer erros encontrados durante solicitações à API são exibidos no console.
Dependências
O programa usa a biblioteca cliente Java da OpenAI para interagir com a API GPT-3.5 Turbo da OpenAI.
A biblioteca da OpenAI lida com a comunicação da API, incluindo o envio da entrada do usuário e o recebimento de respostas geradas pelo modelo.
Variáveis de Ambiente
A chave de API da OpenAI é armazenada como uma variável de ambiente chamada API_KEY_AI.
Certifique-se de que a chave da API esteja corretamente configurada para permitir a comunicação com a API da OpenAI.
Estrutura de Arquivos
O programa cria um diretório "relatorios" dentro de "src/main/resources" para armazenar arquivos de log.
O arquivo de log, chamado relatorio.txt, contém detalhes sobre cada interação do usuário, incluindo linguagem, entrada do usuário e resposta da API.
Conclusão
BotCorrecao oferece uma maneira simples e interativa de consultar o modelo OpenAI GPT-3.5 Turbo para obter informações sobre linguagens de programação e tópicos de codificação. O design modular do programa facilita a manutenção e a extensão para futuras melhorias.
