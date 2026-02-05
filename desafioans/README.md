# Desafio Técnico ANS 2026 - Módulo de Integração e ETL

![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green?style=for-the-badge&logo=spring)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D?style=for-the-badge&logo=swagger)

Este projeto compõe a solução para as etapas 1 e 2 do processo seletivo. Trata-se de uma aplicação robusta em **Java 21** com **Spring Boot** responsável pela extração (Web Scraping), normalização e consolidação de dados contábeis da API de Dados Abertos da ANS.

## Funcionalidades Principais

* **Crawler Automatizado:** Navegação inteligente nos diretórios da ANS (FTP/HTTP) utilizando `JSoup`, resiliente a mudanças na estrutura de pastas (Ano/Trimestre).
* **ETL Incremental:** Download, extração de ZIPs e processamento de CSVs realizados em streaming.
* **Padronização de Dados:** Normalização de arquivos heterogêneos (.csv, .txt, .xlsx) e unificação em layout padrão.
* **Documentação Automática:** API documentada via OpenAPI/Swagger.

---

## Arquitetura e Decisões Técnicas (Trade-offs)

Conforme solicitado no desafio, abaixo estão as justificativas para as abordagens adotadas:

### 1. Processamento de Arquivos: Streaming vs. Memória
* **Decisão:** Utilização de **Streaming** (via `OpenCSV` e `InputStream`).
* **Justificativa:** O volume de dados somado dos trimestres poderia exceder a memória Heap padrão da JVM. O processamento linha a linha garante que a aplicação mantenha um *footprint* de memória baixo e constante, independentemente do tamanho dos arquivos de entrada.

### 2. Normalização de Colunas
* **Decisão:** Mapeamento dinâmico por nome do cabeçalho (*Header Mapping*).
* **Justificativa:** Identifiquei que a ordem das colunas nos arquivos da ANS varia entre os trimestres. O mapeamento fixo por índice causaria erros de leitura; o mapeamento por nome garante a integridade dos dados.

### 3. Tratamento de Inconsistências
Durante a consolidação, as seguintes regras de negócio foram aplicadas:

| Inconsistência Encontrada | Estratégia Adotada |
| :--- | :--- |
| **CNPJ Duplicado** | O registro é mantido, mas logado como `WARN/SUSPEITO` para auditoria futura. |
| **Valores Negativos** | Ignorados. Despesas com sinistros devem ser positivas; valores negativos foram considerados estornos contábeis fora do escopo. |
| **Datas Inconsistentes** | Normalizadas para o padrão ISO-8601 (`YYYY-MM-DD`). |

---

## Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.x
* **Scraping:** JSoup
* **Processamento de Dados:** OpenCSV, Apache Commons Compress (Zip), Apache POI (Excel)
* **Documentação:** Springdoc OpenAPI (Swagger UI)

---

## Documentação da API (Swagger)

A aplicação expõe uma interface interativa para execução e teste dos endpoints.
Após iniciar a aplicação, acesse:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Endpoint Principal
* `POST /api/ans/consolidar`: Dispara o processo completo de ETL.
    * Retorna: Status do processamento e caminho dos arquivos gerados.

---

## Como Executar

### Pré-requisitos
* Java JDK 21 instalado.
* Maven 3.8+ instalado.

### Passo a passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/josiasdev/teste-estagio-2026
    cd teste-estagio-2026/desafioans
    ```

2.  **Compile e Execute:**
    ```bash
    mvn spring-boot:run
    ```

3.  **Dispare o Processo:**
    Você pode usar o **Swagger** (link acima) ou via cURL:
    ```bash
    curl -X POST http://localhost:8080/api/ans/consolidar
    ```

4.  **Verifique a Saída:**
    O arquivo final ('consolidado_despesas.zip') será gerado na pasta raiz do projeto.