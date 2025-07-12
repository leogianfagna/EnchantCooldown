<div align="center">
  <h1>⏰ EnchantCooldown</h1>
  <p>Minecraft Mod para controlar tempo de espera de encantamentos.</p>

[![Tecnologias](https://skillicons.dev/icons?i=java,gradle)](https://skillicons.dev)

</div>

Esse projeto cria um mod para Minecraft Fabric, que exibe o tempo de espera de encantamentos do plugin AdvancedEnchantments, para as versões mais atuais do jogo. O propósito é auxiliar e melhorar a jogatina, com configurações flexíveis pelos usuários.

![GitHub contributors](https://img.shields.io/github/created-at/leogianfagna/EnchantCooldown)
![GitHub contributors](https://img.shields.io/github/contributors-anon/leogianfagna/EnchantCooldown)
![GitHub commit activity](https://img.shields.io/github/commit-activity/t/leogianfagna/EnchantCooldown)
![GitHub contributors](https://img.shields.io/github/issues/leogianfagna/EnchantCooldown)
![GitHub Release](https://img.shields.io/github/v/release/leogianfagna/EnchantCooldown)
![GitHub Repo stars](https://img.shields.io/github/stars/leogianfagna/EnchantCooldown)

<div align="center">
  <h1>🔥 Features</h1>
</div>

O objetivo do mod é disponibilizar flexibilidade para os usuários customizarem suas preferências. Nem sempre queremos exibir todos os tempos de recarga, dado o fato de que pode ficar poluído, o que nunca é bom durante um combate, não é mesmo?

### ⏳ Tempo de recarga

Exibe a contagem regressiva do tempo de recarga após ele ser ativado. Rastrear esse tempo pode ser muito útil para montar combos específicos, se preparar para situações ou até alterar entre itens enquanto alguns encantamentos estão na espera.
<br></br>
<img width="320" height="69" alt="image" src="https://github.com/user-attachments/assets/af97bc5c-0797-45b5-a4a3-1e61b7bc322a" />

### 📜 Lista customizada

O usuário escolhe quais encantamentos quer rastrear, em vez de rastrear todos os encantamentos do servidor. Isso é essencial para o funcionamento, pois os servidores não disponibilizam o tempo de recarga de cada encantamento, então passa a ser trabalho do usuário definir o tempo de cada um e qual quer rastrear.
<br></br>
<img width="298" height="332" alt="image" src="https://github.com/user-attachments/assets/fa5e43a4-eace-43c8-9135-485dee080ddb" />

### 🧶 A fazer

Você pode propor **sugestões**, seja me enviando mensagens ou [adicionando-as aqui](https://github.com/leogianfagna/EnchantCooldown/issues/new?template=1-sugest.yml), ou também propor mudanças no plugin através de pull requests, já que é código aberto! Da minha parte, está proposto para esse mod ainda possuir as seguintes funcionalidades (sinta-se convidado a me ajudar a desenvolver):

- Adição de encantamentos via menu customizado.
- Possibilidade de alterar o tempo de recarga direto no menu sem necessidade de refazer a configuração.
- Habilitar/desabilitar rastreamentos sem a necessidade de apagá-los da sua configuração.
- Ícones mostrando qual é a aplicação daquele encantamento e em qual item ele está.
- Rastreamento profundo, que o usuário pode escolher alguns encantamentos para esse tópico. Esses encantamentos ficarão na tela 100% das vezes, mostrando como "Pronto" se não estão em recarga. Isso pode ser útil para encantamentos específicos que exigem uma estratégia separada.

<div align="center">
  <h1>📥 Instalação e uso</h1>
</div>
Fique atento com as especificações de uso do mod, que exige Fabric e possui versões específicas:

### 🖥️ Download

- **Versão estável**: Disponível para [descarregar aqui](https://github.com/leogianfagna/EnchantCooldown/releases), as versões estáveis são sempre separadas com um patch notes e lançadas com controles de versão e grandes alterações.
- **Versão dev**: Também é possível instalar a build compilada a cada alteração de código, que são úteis para testar as últimas mudanças antes que elas sejam empacotadas em uma versão. Os artefatos gerados ficam para livre uso [aqui](https://github.com/leogianfagna/EnchantCooldown/actions).

### 🛠️ Dependências e versões testadas

O mod não foi testado em diferentes versões e nem contextos, a versão recomendada e suas dependências são:

- [Fabric](https://fabricmc.net/use/installer/).
- Versão do jogo: 1.21.1.

### 🧩 Configuração

Até o momento, o mod não possui menus customizados (mas se encontra em desenvolvimento). A configuração de encantamentos e suas recargas são feitas através dos comandos do mod.
| Comando                       | Descrição                                                             | Exemplo                   |
|-------------------------------|-----------------------------------------------------------------------|---------------------------|
| /ecd list                     | Exibe a lista de todos os encantamentos registrados pelo usuário.     | /ecd list                 |
| /ecd set <enchant> <cooldown> | Adiciona um encantamento na lista de recargas (recarga em segundos).  | /ecd set "Furacão" 25     |
| /ecd set remove <enchant>     | Remove um encantamento da lista de recargas.                          | /ecd set remove "Furacão" |

> [!NOTE]  
> Utilize o nome do encantamento entre `"` quando ele possui caracteres especiais (acentuação ou ç) ou nome composto (quando existe espaço).

<div align="center">
  <h1>⭐ Showcase</h1>
</div>
<img width="1492" height="1004" alt="image" src="https://github.com/user-attachments/assets/2c1f2c41-f7b4-455e-9264-7215f82f1880" />
