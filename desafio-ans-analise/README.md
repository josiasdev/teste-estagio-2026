# Desafio 2 - Transformação, Enriquecimento e Agregação ANS

Este módulo realiza o processamento avançado dos dados contábeis da ANS, integrando as demonstrações financeiras (consolidadas no Desafio 1) com os dados cadastrais das operadoras ativas.

## Tecnologias Utilizadas
* **Java 17** com **Spring Boot 3**
* **OpenCSV**: Para leitura e escrita performática de arquivos CSV, garantindo o tratamento correto de delimitadores e aspas.
* **Springdoc-openapi (Swagger)**: Documentação automatizada e interface para execução dos endpoints.
* **Lombok**: Para redução de código boilerplate em DTOs e Services.

---

## Decisões Técnicas e Trade-offs

### 1. Validação de Dados (Item 2.1)
* **Estratégia:** Foi utilizado o padrão **Strategy**, permitindo que múltiplas regras de validação (CNPJ, Razão Social, Valores Positivos) sejam aplicadas de forma desacoplada e extensível.
* **Trade-off:** Optei pela **Filtragem Estrita**. Registros com CNPJ matematicamente inválidos ou campos obrigatórios vazios são descartados da agregação final.
* **Justificativa:** Garantir a integridade estatística do resultado, evitando que dados de teste ou corrompidos influenciem as médias e desvios padrão.

### 2. Enriquecimento e Join (Item 2.2)
* **Estratégia:** Implementação de um **Hash Join em memória**.
* **Justificativa:** O arquivo de cadastro (`Relatorio_cadop.csv`) possui um volume de dados que cabe facilmente na memória RAM. O uso de um `Map<String, DTO>` permite que o cruzamento de informações ocorra em tempo constante $O(1)$ para cada linha do arquivo de despesas.
* **Tratamento de Chaves:** Implementei uma lógica de **Normalização de Chave** (padding de 6 dígitos) para o Registro ANS. Isso resolve a inconsistência onde o consolidado contém apenas o número do registro e o cadastro oficial contém o CNPJ completo.

### 3. Agregação e Estatísticas (Item 2.3)
* **Precisão Financeira:** Substituímos o uso de `Double` por **`BigDecimal`** com arredondamento `HALF_UP`.
* **Justificativa:** O uso de `BigDecimal` elimina a exibição em notação científica (ex: `1.43E9`) no CSV final e garante que cálculos envolvendo bilhões de reais não sofram com perdas de precisão de ponto flutuante.
* **Cálculos:** Foram implementados cálculos de Soma Total, Média Trimestral e Desvio Padrão Amostral, agrupados por Razão Social e UF.

---

## Estrutura do Projeto
```text
src/main/java/com/github/josiasdev/desafio_ans_analise/
├── config/             # Configurações de IO e Swagger
├── controller/         # Endpoints da API (v2)
├── dto/                # Mapeamento de entrada e saída (OpenCSV)
├── service/            
│   ├── aggregation/    # Cálculos estatísticos (Média/Desvio)
│   ├── ans/            # Crawler do Relatorio_cadop.csv
│   ├── enrichment/     # Lógica de Join (Match de registros)
│   ├── orquestracao/   # Gestão do fluxo completo de processamento
│   └── validation/     # Validadores de CNPJ e integridade
└── util/               # Algoritmo de validação de dígitos do CNPJ
```


## Como Executar
- Certifique-se de que o arquivo consolidado.csv (gerado no Desafio 1) está presente na raiz do projeto.
- Execute a aplicação Spring Boot.
- Acesse a interface do Swagger em: http://localhost:8080/swagger-ui/index.html.
- Localize o grupo ANS Desafio 2 e execute o endpoint POST /api/ans/v2/processar.

### O sistema irá:
- Baixar o cadastro de operadoras atualizado.
- Validar e enriquecer os dados.
- Gerar o arquivo despesas_agregadas.csv e o arquivo compactado Teste_Josias.zip na pasta dados_ans/output/.