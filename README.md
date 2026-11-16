# Gestão de Laboratório Óptico

Trabalho final de ADS — Otávio Bombana.

Sistema web para um laboratório óptico controlar as óticas clientes, as lentes e armações
que produz/monta e os **pedidos de lentes** feitos pelas óticas.

## Tecnologias

- Jakarta EE 10 (JSF/Faces 4, EJB, JPA, CDI)
- PrimeFaces 13 + Template Manhattan
- PostgreSQL
- GlassFish 7
- Java 21, Maven

## Requisitos do trabalho

| Requisito | Onde está |
|---|---|
| Login com filtro administrativo | `login.xhtml`, `LoginController`, `filter/AdminFilter` (protege tudo em `/admin/*`) |
| Enum | `model/StatusPedido` (Recebido, Em produção, Pronto, Entregue, Cancelado) |
| 3 CRUDs básicos | Óticas, Lentes e Armações (`admin/oticas.xhtml`, `admin/lentes.xhtml`, `admin/armacoes.xhtml`) |
| Funcionalidade principal com one-to-many | Pedido de lentes: um `Pedido` tem vários `ItemPedido` (`admin/pedidos.xhtml`) |
| Persistência em PostgreSQL | `META-INF/persistence.xml` + datasource em `WEB-INF/web.xml` |

## Como rodar

1. **Banco:** no PostgreSQL, crie o banco:
   ```sql
   CREATE DATABASE laboratorio_optico;
   ```
   As tabelas são criadas sozinhas no primeiro deploy.

2. **Usuário e senha do Postgres:** abra `src/main/webapp/WEB-INF/web.xml` e ajuste
   `<user>` e `<password>` dentro de `<data-source>` (o padrão está `postgres` / `postgres`).

3. **Gerar o .war:**
   ```
   mvn clean package
   ```
   O arquivo sai em `target/LaboratorioOptico.war`.

4. **Deploy no GlassFish 7:** inicie o domínio (`asadmin start-domain`) e faça o deploy:
   ```
   asadmin deploy --force=true target/LaboratorioOptico.war
   ```
   (ou pelo console em http://localhost:4848 → Applications → Deploy).

5. **Acessar:** http://localhost:8080/LaboratorioOptico/

   Login inicial: **admin** / **admin123** (criado automaticamente no primeiro deploy).

### Se o deploy reclamar do driver do PostgreSQL

Copie o `postgresql-42.7.7.jar` (fica em `target/LaboratorioOptico/WEB-INF/lib/` depois do build)
para `glassfish7/glassfish/domains/domain1/lib/`, reinicie o GlassFish e faça o deploy de novo.

## Estrutura

```
src/main/java/br/com/laboratoriooptico/
  model/       entidades (Otica, Lente, Armacao, Pedido, ItemPedido, Usuario) e o enum StatusPedido
  facade/      acesso ao banco (EJB @Stateless)
  controller/  beans das telas (@Named)
  converter/   conversores para usar entidades nos p:selectOneMenu
  filter/      AdminFilter (filtro administrativo)
  config/      DadosIniciais (cria o admin) e Senha (hash SHA-256)
src/main/webapp/
  login.xhtml
  admin/       páginas protegidas pelo filtro
  WEB-INF/     template Manhattan (topbar, sidebar...), web.xml, beans.xml
```
