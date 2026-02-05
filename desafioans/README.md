# Desafio Técnico ANS - Consolidação de Despesas

Este projeto realiza a extração automática, normalização e consolidação de dados contábeis da API de Dados Abertos da ANS utilizando **Java 21** e **Spring Boot**.

## Arquitetura e Decisões Técnicas

- **Processamento Incremental:** Devido ao grande volume de dados dos CSVs da ANS, a leitura e escrita são feitas via streaming (OpenCSV), evitando estouro de memória (Heap).
- **Crawler Resiliente:** Utiliza JSoup para navegar na estrutura de diretórios do servidor FTP/HTTP, adaptando-se a variações de pastas.
- **Normalização Dinâmica:** Identifica colunas por nome (header mapping) em vez de índice fixo, suportando variações de layout entre trimestres.

## 🛠ecnologias
- Spring Boot 3
- JSoup (Web Scraping)
- OpenCSV (Manipulação de arquivos)
- Apache Commons Compress (Extração de ZIP)

## Tratamento de Inconsistências
| Caso | Tratamento |
|---|---|
| CNPJ duplicado com razão diferente | Mantido o original, marcado como `SUSPEITO` no log. |
| Valores negativos | Ignorados (log de aviso), pois despesas de sinistros devem ser positivas. |
| Formatos de Data | Normalizados para o padrão ISO-8601. |

## Como executar
1. Clone o repositório.
2. Execute `mvn spring-boot:run`.
3. Chame o endpoint: `POST http://localhost:8080/api/ans/consolidar`.