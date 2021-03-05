# Schlock Bort

Hi there.  Recently I've been streaming on Twitch, so I decided to write my own bot to do various things. This bot does 3 things: connects to Twitch, connects to Discord, and hosts webpages to be used as OBS overlays.

* Follow me on Twitch here: http://twitch.tv/dyreschlock
* Follow me on Twitter, too: http://twiter.com/dyreschlock
* Join the Discord here: https://discord.gg/7GZ7xdR5yK

## Twitch Commands

The following are commands that should work in Twitch.

**Points management** - Manage that points you current have in Schlock Bot.

* **!balance** -- current point total in Schlock Bot
* **!cashout [points]** -- takes your points in Schlock Bot and moves them to Stream Elements
* **!givepoints schlock_bot [points]** -- takes your points in Stream Elements and moves them to Schlock Bot


**Shiny Information** - Get information on my shiny progress in Let's Go Eevee.

* **!last** (or) **!recent** -- returns the most recent shiny
* **!shinyaverage** -- returns my average time for any shiny to appear
* **!shinychecks** -- return my average number of checks for a rare shiny
* **!shinydex** -- returns the current count of pokemon in my shiny dex

**Shiny Bets!** - You can bet points on which shiny pokemon will show up in my game next, and in how many minutes. If you guess the pokemon correctly, you'll double your bet. If you guess the closest number of minutes, you'll triple your bet. And if you get both, you'll triple your winnings.  Betting is opened and closed by the admin.

Betting information, details, and payouts are relayed to both Twitch and Discord.

* **!bet [pokemon] [minutes] [points]** -- make a shiny bet. Bet points that the next shiny will be [pokemon] and it'll appear in [minutes].
* **!cancelbet [pokemon]** -- cancel a single bet on a specific pokemon.
* **!cancelallbets** -- cancel all of your current bets
* **!currentbets** -- view all of your current bets


**Who's that Pokemon?** - Guess on a random pokemon for 20 points. The Bot will listen to all messages and when any text matches the current pokemon, points will be awarded.

* **!whodat** -- Starts the game with a random pokemon from all generations.
* **!whodat [gen]** -- Starts the game with a random pokemon a particular generation.
* **!whodat [gen1-gen2]** -- Start the game with a range of generations.


**Pokemon Information** - Return information from a pokemon, or a random pokemon

* **!pokemon [number of name]** -- return details of a specific pokemon
* **!pokemon random** -- return a random pokemon
* **!pokemon [range]** -- return a random pokemon from a range. (ie 1-23, bulbasaur-rayquaza, whatevs)


## Animations and Sound Effects

Spend some of your points to play animations or sound effects on my stream.

* coming soon

## Discord Commands

The following commands should work in Discord when the bot is running, but only in the #bot-chat-relay channel.

**Shiny Information** - Get information on my shiny progress in Let's Go Eevee.

* **!last** (or) **!recent** -- returns the most recent shiny
* **!shinyaverage** -- returns my average time for any shiny to appear
* **!shinychecks** -- return my average number of checks for a rare shiny
* **!shinydex** -- returns the current count of pokemon in my shiny dex

**Pokemon Information** - Return information from a pokemon, or a random pokemon

* **!pokemon [number of name]** -- return details of a specific pokemon
* **!pokemon random** -- return a random pokemon
* **!pokemon [range]** -- return a random pokemon from a range. (ie 1-23, bulbasaur-rayquaza, whatevs)

