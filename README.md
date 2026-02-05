# Teste Processo Seletivo 2026 - Estágio Desenvolvedor Fullstack

Este repositório contém as soluções desenvolvidas para o processo seletivo, combinando a robustez do ecossistema **Java (Spring Boot)** para Engenharia de Dados com **Python** e **Vue.js** para a interface web.

## Desafios

1.  **Teste de Integração com API Pública (Java/Spring Boot)**: Aplicação desenvolvida em Java que consome a API de Dados Abertos da ANS. O sistema realiza o download automatizado e resiliente das Demonstrações Contábeis, normaliza arquivos de formatos variados (CSV, TXT, XLSX) e consolida os dados de despesas em um arquivo único padronizado.
2.  **Teste de Transformação e Validação de Dados (Java/Spring Boot)**: Módulo de processamento em Java que aplica regras de validação estritas (CNPJs, valores numéricos), realiza o enriquecimento dos dados através de cruzamento (*join*) com o cadastro de operadoras ativas e gera agregações estatísticas por UF e Operadora.
3.  **Teste de Banco de Dados e Análise (SQL)**: Scripts responsáveis pela modelagem do banco (DDL), importação eficiente dos dados tratados e execução de queries analíticas para identificar padrões de crescimento e distribuição geográfica.
4.  **Teste de API e Interface Web (Python/Vue.js)**: API RESTful e Dashboard interativo para busca de operadoras e visualização de dados.

---

## Tecnologias Utilizadas

* **ETL & Processamento:** Java 17+, Spring Boot, Apache POI (para leitura de .xlsx), Apache Commons CSV.
* **Aplicação Web:** Python (FastAPI/Flask) e Vue.js.
* **Banco de Dados:** PostgreSQL / MySQL.
* **Ferramentas:** Maven, Docker, Git.


