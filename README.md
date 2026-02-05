# Teste Processo Seletivo 2026 - Estágio Desenvolvedor Fullstack

Este repositório contém as soluções desenvolvidas para o processo seletivo, abrangendo engenharia de dados (ETL), modelagem SQL e desenvolvimento de aplicação web completa (Backend Python e Frontend Vue.js).

## Desafios

1.  **Teste de Integração com API Pública**: O código realiza o acesso à API de Dados Abertos da ANS, faz o download automatizado e resiliente das Demonstrações Contábeis, normaliza arquivos de formatos variados (CSV, TXT, XLSX) e consolida os dados de despesas em um arquivo único padronizado.
2.  **Teste de Transformação e Validação de Dados**: O código aplica regras de validação (CNPJs, valores numéricos), realiza o enriquecimento dos dados através de cruzamento (join) com o cadastro de operadoras ativas e gera agregações estatísticas por UF e Operadora.
3.  **Teste de Banco de Dados e Análise**: Scripts SQL responsáveis pela modelagem do banco (DDL), importação eficiente dos dados tratados e execução de queries analíticas complexas para identificar padrões de crescimento de despesas e distribuição geográfica.
4.  **Teste de API e Interface Web**: Desenvolvimento de uma API RESTful (Python) e um Dashboard interativo (Vue.js) que permite a busca paginada de operadoras, visualização de histórico de despesas e gráficos estatísticos.

---

## 🛠 Tecnologias Utilizadas
* **Linguagem:** Python 3.x
* **Banco de Dados:** MySQL 8.0 / PostgreSQL (definir qual usou)
* **Backend:** FastAPI (ou Flask)
* **Frontend:** Vue.js
* **Ferramentas:** Docker, Pandas, SQLAlchemy.

## 📋 Documentação de Decisões Técnicas (Trade-offs)
*Conforme solicitado no desafio, as justificativas para as escolhas de arquitetura, paginação e normalização estão detalhadas abaixo:*

* **Processamento de Arquivos:** [Ex: Processamento em memória vs incremental...]
* **Tratamento de Inconsistências:** [Ex: Estratégia adotada para CNPJs duplicados...]
* **Banco de Dados:** [Ex: Escolha entre normalização vs desnormalização...]
* **Estratégia de API:** [Ex: Escolha do framework e tipo de paginação...]

## 🚀 Como Executar

### 1. ETL e Transformação
```bash
# Instalar dependências e rodar scripts
pip install -r requirements.txt
python scripts/etl_ans.py
