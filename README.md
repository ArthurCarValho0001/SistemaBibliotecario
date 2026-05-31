# 📚 Biblioteca UDF

Sistema de gerenciamento de biblioteca acadêmica desenvolvido em Java com interface gráfica Swing e banco de dados SQLite. Permite cadastro de livros e usuários, controle de empréstimos, verificação de atrasos e fila de espera automática.

---

## 🖥️ Demonstração

> Ao abrir o sistema, quatro abas estão disponíveis: **Livros**, **Usuários**, **Empréstimos** e **Fila de Espera**.

---

## 🚀 Como executar

### Pré-requisitos

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition é suficiente)
- Driver SQLite: `sqlite-jdbc-3.53.1.0.jar` (já incluído na pasta `lib/`)

### Rodando pelo IntelliJ

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/bibliotecaJava.git

# 2. Abra o projeto no IntelliJ IDEA
# File → Open → selecione a pasta bibliotecaJava

# 3. Confirme que o driver SQLite está configurado
# File → Project Structure → Libraries → deve aparecer sqlite-jdbc

# 4. Execute a classe principal
# src/main/Main.java → botão Run
```

> Na primeira execução, o arquivo `biblioteca.db` é criado automaticamente na raiz do projeto com todas as tabelas.

---

## 🗂️ Estrutura do Projeto

```
bibliotecaJava/
├── src/
│   ├── main/
│   │   └── Main.java               # Ponto de entrada da aplicação
│   ├── model/
│   │   ├── Usuario.java            # Classe abstrata base de usuário
│   │   ├── Aluno.java              # Herda de Usuario
│   │   ├── Professor.java          # Herda de Usuario, tem departamento
│   │   ├── Livro.java              # Entidade livro
│   │   ├── Emprestimo.java         # Entidade empréstimo com datas e status
│   │   ├── FilaEspera.java         # Entidade de posição na fila
│   │   └── StatusEmprestimo.java   # Enum com os status possíveis
│   ├── dao/
│   │   ├── LivroDAO.java           # Operações SQL de livros
│   │   ├── UsuarioDAO.java         # Operações SQL de usuários
│   │   ├── EmprestimoDAO.java      # Operações SQL de empréstimos
│   │   └── FilaEsperaDAO.java      # Operações SQL da fila de espera
│   ├── service/
│   │   ├── LivroService.java       # Regras de negócio de livros
│   │   ├── UsuarioService.java     # Regras de negócio de usuários
│   │   └── EmprestimoService.java  # Regras de negócio de empréstimos
│   ├── ui/
│   │   ├── TelaInicial.java        # Janela principal com abas
│   │   ├── TelaCadastroLivro.java  # Aba de livros
│   │   ├── TelaCadastroUsuario.java# Aba de usuários
│   │   ├── TelaEmprestimo.java     # Aba de empréstimos
│   │   └── TelaFilaEspera.java     # Aba de fila de espera
│   └── util/
│       ├── ConexaoDB.java          # Singleton de conexão com SQLite
│       └── InicializadorDB.java    # Criação das tabelas na primeira execução
├── lib/
│   └── sqlite-jdbc-3.53.1.0.jar   # Driver SQLite
├── biblioteca.db                   # Banco de dados (gerado automaticamente)
└── README.md
```

---

## 🏗️ Arquitetura em Camadas

O projeto segue o padrão de arquitetura em camadas, separando responsabilidades de forma clara:

### 📦 Model
Representa as entidades do sistema como objetos Java. Não contém lógica de banco nem regras de negócio — apenas descreve o que cada coisa é.

### 📦 DAO (Data Access Object)
Toda e qualquer linha de SQL fica nessa camada. Recebe objetos Java, persiste no banco e reconstrói objetos a partir dos dados armazenados.

### 📦 Service
Cérebro da aplicação. Contém todas as regras de negócio — validações, decisões de fluxo e coordenação entre DAOs. A interface nunca acessa o DAO diretamente.

### 📦 UI (Interface Swing)
Responsável apenas por exibir informações e capturar ações do usuário. Delega toda lógica para os Services.

### 📦 Util
Infraestrutura compartilhada: conexão com banco de dados (padrão Singleton) e inicialização das tabelas.

```
UI → Service → DAO → Banco de Dados
                ↑
             Model (trafega entre todas as camadas)
```

---

## ✨ Funcionalidades

### 📖 Livros
- Cadastrar livro com título, autor, ano de publicação e editora
- Listar todos os livros em tabela
- Status visual: **verde** para disponível, **vermelho** para emprestado
- Validação de campos obrigatórios

### 👤 Usuários
- Cadastrar alunos e professores
- Campo de departamento aparece automaticamente ao selecionar Professor
- Validações: CPF com 11 dígitos, e-mail com @, matrícula única
- Listar todos os usuários em tabela

### 🔄 Empréstimos
- Realizar empréstimo por matrícula do usuário e ID do livro
- Prazo automático de 30 dias para devolução
- Se o livro estiver indisponível, oferece entrada na fila automaticamente
- Registrar devolução por ID do empréstimo
- Ao devolver, o próximo da fila é notificado e o empréstimo é ativado automaticamente
- Status colorido na tabela: **verde** ATIVO, **laranja** RESERVADO, **vermelho** ATRASADO, **cinza** DEVOLVIDO/CANCELADO
- Verificação automática de atrasos ao abrir o sistema

### ⏳ Fila de Espera
- Consultar fila de um livro pelo ID
- Visualizar posição, usuário, livro e data de entrada
- Cancelar reserva pelo ID do empréstimo

---

## 🔢 Status de Empréstimo

| Status | Descrição |
|--------|-----------|
| `ATIVO` | Livro em posse do usuário, dentro do prazo |
| `DEVOLVIDO` | Empréstimo concluído normalmente |
| `ATRASADO` | Prazo de 30 dias expirado, livro não devolvido |
| `RESERVADO` | Usuário na fila, aguardando o livro ficar disponível |
| `CANCELADO` | Usuário desistiu da fila antes de receber o livro |

---

## 🗄️ Banco de Dados

O sistema utiliza **SQLite** — banco de dados local em arquivo, sem necessidade de instalação de servidor.

### Tabelas

**usuarios**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | INTEGER PK | Identificador único |
| nome | TEXT | Nome completo |
| cpf | TEXT | CPF (11 dígitos) |
| email | TEXT UNIQUE | E-mail |
| matricula | TEXT UNIQUE | Matrícula |
| tipo | TEXT | "Aluno" ou "Professor" |
| departamento | TEXT | Apenas para Professor |

**livros**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | INTEGER PK | Identificador único |
| titulo | TEXT | Título do livro |
| autor | TEXT | Nome do autor |
| ano_publicacao | INTEGER | Ano de publicação |
| editora | TEXT | Nome da editora |
| disponivel | INTEGER | 1 = disponível, 0 = emprestado |

**emprestimos**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | INTEGER PK | Identificador único |
| usuario_id | INTEGER FK | Referência ao usuário |
| livro_id | INTEGER FK | Referência ao livro |
| data_emprestimo | TEXT | Data de início |
| data_devolucao_prevista | TEXT | Data limite (30 dias) |
| data_devolucao_real | TEXT | Data efetiva de devolução |
| status | TEXT | Status atual do empréstimo |

**fila_espera**
| Campo | Tipo | Descrição |
|-------|------|-----------|
| id | INTEGER PK | Identificador único |
| livro_id | INTEGER FK | Referência ao livro |
| usuario_id | INTEGER FK | Referência ao usuário |
| data_entrada | TEXT | Data de entrada na fila |
| posicao | INTEGER | Posição na fila |

---

## 🧱 Conceitos de POO Aplicados

- **Herança** — `Aluno` e `Professor` herdam de `Usuario`
- **Polimorfismo** — método abstrato `getTipoUsuario()` implementado diferente em cada subclasse; reconstrução do tipo correto no `UsuarioDAO`
- **Encapsulamento** — todos os atributos privados com getters e setters
- **Singleton** — `ConexaoDB` garante uma única conexão com o banco em todo o sistema

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Swing** — interface gráfica
- **SQLite** — banco de dados local
- **JDBC** — comunicação com o banco
- **IntelliJ IDEA** — ambiente de desenvolvimento

---

## 👥 Autores

Desenvolvido como projeto acadêmico para a disciplina de **Programação Orientada a Objetos** — UDF.

---

## 📄 Licença

Este projeto é de uso acadêmico.
