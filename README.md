# Análise de Desempenho de Lista Encadeada com Java e JNI
Descrição:

Este projeto é uma demonstração avançada de análise de desempenho e interoperabilidade de linguagens, implementando uma estrutura de dados de Lista Encadeada em Java com uma biblioteca nativa em C++ para medição de tempo de execução. O programa opera de forma não interativa, carregando um estado inicial a partir de um arquivo de texto e executando uma sequência de comandos de manipulação (adicionar, remover, imprimir) a partir de um segundo arquivo. A principal característica do projeto é o uso da JNI (Java Native Interface) para invocar uma função C++ que acessa diretamente a API do Windows (GetProcessTimes). Isso permite a captura precisa dos tempos de execução de usuário (user) e sistema (sys), métricas que não são diretamente acessíveis através do Java padrão. O projeto serve como um excelente estudo de caso para conceitos de estruturas de dados, integração de código nativo, compilação multi-linguagem e a resolução de desafios de configuração de ambiente.

O programa é totalmente automatizado. Ele inicializa a lista com dados de `lista_inicial.txt` e, em seguida, executa uma série de operações de adição, remoção e impressão definidas no arquivo `comandos.txt`. Ao final, ele exibe uma análise detalhada do tempo de execução.

## Como Funciona a Medição de Desempenho?

O Java padrão não consegue separar o tempo de CPU gasto em modo de usuário (`user`) e em modo de sistema (`sys`). Para contornar essa limitação, este projeto utiliza a seguinte arquitetura:
1.  **Java (`App.java`)**: Orquestra a lógica principal da aplicação.
2.  **JNI Bridge (`TimeUtils.java`)**: Declara um método `native` que serve como ponte para o código C++.
3.  **Biblioteca C++ (`TimeUtils.dll`)**: Implementa o método nativo, fazendo uma chamada direta à função `GetProcessTimes` da API do Windows para obter os tempos precisos.
4.  **JVM**: Carrega a DLL em tempo de execução, permitindo que o Java execute a função C++ como se fosse sua.

## Funcionalidades Principais

- **Estrutura de Dados:** Implementação de Lista Encadeada simples em Java.
- **Execução Automatizada:** Operação não interativa baseada em arquivos de entrada.
- **Integração Multi-linguagem:** Exemplo prático de JNI para conectar Java e C++.
- **Análise de Desempenho Precisa:** Medição e exibição dos tempos `real`, `user` e `sys`.
- **Tratamento de Erros:** Gestão de operações inválidas, como inserção em posições inexistentes.

## Pré-requisitos

Para compilar и executar este projeto, seu ambiente precisa ter:

- **Sistema Operacional:** Windows (devido à chamada de API específica).
- **JDK (Java Development Kit):** Recomenda-se a versão 21 ou superior. É **essencial** que a variável de ambiente `JAVA_HOME` esteja configurada e que a pasta `bin` do JDK esteja no `PATH` do sistema.
- **Compilador C++:** **MinGW-w64** (fornecendo o `g++.exe`). É necessário que a pasta `bin` do MinGW-w64 também esteja no `PATH` do sistema.

## Instalação e Execução

Siga este guia detalhado para rodar o projeto. Execute todos os comandos no terminal (PowerShell ou CMD) dentro da pasta do projeto.

### Passo 1: Clone o Repositório

```bash
git clone [URL_DO_SEU_REPOSITORIO]
cd [NOME_DA_PASTA_DO_REPOSITORIO]
```

### Passo 2: Compile o Código Java e Gere o Header JNI

Primeiro, compile todos os arquivos `.java` para gerar os `.class`. Em seguida, gere o arquivo de cabeçalho C++ (`.h`) necessário para a JNI.

```powershell
# Compila todos os arquivos .java
javac *.java

# Gera o arquivo TimeUtils.h a partir da classe TimeUtils.java
javac -h . TimeUtils.java
```

### Passo 3: Compile a Biblioteca Nativa C++ (DLL)

Este processo é feito em duas etapas. A primeira compila o código C++ para um "arquivo objeto" e a segunda o transforma em uma biblioteca DLL que o Java pode usar.

```powershell
# 1. Compila o .cpp para um arquivo objeto (.o), incluindo os headers do JNI
# Usamos a sintaxe do PowerShell para a variável de ambiente
g++ -c -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" TimeUtils.cpp -o TimeUtils.o

# 2. Cria a biblioteca compartilhada (.dll) a partir do arquivo objeto
# As aspas no último argumento são importantes para o PowerShell
g++ -shared -o TimeUtils.dll TimeUtils.o "-Wl,--add-stdcall-alias"
```

### Passo 4: Execute a Aplicação Java

Finalmente, execute o programa. Usamos dois argumentos importantes para a JVM:
- `-cp .` (classpath): Diz ao Java para procurar os arquivos `.class` na pasta atual.
- `"-Djava.library.path=."`: Diz ao Java para procurar os arquivos de biblioteca nativa (`.dll`) na pasta atual. As aspas são para garantir que o PowerShell interprete corretamente.

```powershell
java -cp . "-Djava.library.path=." App
```

## Exemplo de Saída

Uma execução bem-sucedida terá uma saída semelhante a esta:

```
Lista inicial carregada do arquivo 'lista_inicial.txt':
Lista atual: 100.0 200.0 300.0 400.0 500.0

--- Executando Comandos do Arquivo ---
> P
Lista atual: 100.0 200.0 300.0 400.0 500.0
> A 99 1
INFO: Valor 99.0 adicionado na posicao 1.
> R 999
ERRO: Valor 999.0 nao encontrado na lista.
> P
Lista atual: 100.0 99.0 200.0 300.0 400.0 500.0
> R 500
INFO: Valor 500.0 removido da lista.
> P
Lista atual: 100.0 99.0 200.0 300.0 400.0
--------------------------------------

Programa encerrado. Calculando tempos...

--- Tempos de Execucao ---
real    0.015s
user    0.000s
sys     0.015s
```
