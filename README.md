# Schlock Bort

Hi there.  Recently I've been streaming on Twitch, so I decided to write my own bot to do various things. This bot does 3 things: connects to Twitch, connects to Discord, and hosts webpages to be used as OBS overlays.

* Follow me on Twitch here: http://twitch.tv/dyreschlock
* Follow me on Twitter, too: http://twiter.com/dyreschlock
* Join the Discord here: https://discord.gg/7GZ7xdR5yK

## Twitch Commands

The following are commands that should work in Twitch.

**Points management** - Manage the points you currently have in Schlock Bot.

* **!balance** -- current point total in Schlock Bot
* **!prestige** -- when reaching a certain point threshold, you can prestige. Prestiging gives you a star on the leadboard, and ranking priority over other users. (First prestige goal is 500k. Second is 2mil. Third is 4mil, etc)


**Shiny Information** - Get information on my shiny progress 

* Pokemon Legends Arceus
    * **!averagechecks** -- returns my average number of outbreak checks before a shiny appears
* Let's Go Eevee.
    * **!last** (or) **!recent** -- returns the most recent shiny
    * **!shinyaverage** -- returns my average time for any shiny to appear
    * **!shinychecks** -- return my average number of checks for a rare shiny
    * **!shinydex** -- returns the current count of pokemon in my shiny dex

**Shiny Bets!** - You can bet points on which shiny pokemon will show up in my game next, and in how many minutes. If you guess the pokemon correctly, you'll double your bet. If you guess the closest number of minutes, you'll triple your bet. And if you get both, you'll triple your winnings.  Betting is opened and closed by the admin.

Betting information, details, and payouts are relayed to both Twitch and Discord.

* Pokemon Legends Arceus
    * **!bet [points] [number of outbreaks] ([pokemon])** -- make a shiny bet. Bet [points] on a shiny appearing after [number of outbreaks]. Optional, add a [pokemon] to your bet.
    * **!cancelbet** -- cancel your current bet.
* Let's Go Eevee
    * **!bet [pokemon] [minutes] [points]** -- make a shiny bet. Bet points that the next shiny will be [pokemon] and it'll appear in [minutes].
    * **!cancelbet [pokemon]** -- cancel a single bet on a specific pokemon.
    * **!cancelallbets** -- cancel all of your current bets
* Both      
    * **!currentbets** -- view all of your current bets


**Who's that Pokemon?** - Guess on a random pokemon for 20 points. The Bot will listen to all messages and when any text matches the current pokemon, points will be awarded. Streaks will increase point payout.

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

**Points Information** - Get information on Schlock Bot users

* **!balance [twitch username]** -- Get your point balance.
* **!leaderboard** -- Get the current top 10 ranked users by point balance.

**Shiny Information** - Get information on my shiny progress in Let's Go Eevee.

* **!last** (or) **!recent** -- returns the most recent shiny
* **!shinyaverage** -- returns my average time for any shiny to appear
* **!shinychecks** -- return my average number of checks for a rare shiny
* **!shinydex** -- returns the current count of pokemon in my shiny dex

**Shiny Bets** - Get information on current bets

* **!allcurrentbets** -- returns a list of all current bets with their Twitch username

**Pokemon Information** - Return information from a pokemon, or a random pokemon

* **!pokemon [number of name]** -- return details of a specific pokemon
* **!pokemon random** -- return a random pokemon
* **!pokemon [range]** -- return a random pokemon from a range. (ie 1-23, bulbasaur-rayquaza, whatevs)

## Hosted Pages for OBS Overlays

The following are URLs to be used as overlays in OBS.  Accessed locally for the moment.

* **/twitchalerts** -- Currently, this page shows the most recent user in the database. It's meant to be used as "new follower" overlay

* **/base/leaderboard** -- Shows the current points total of the top 10 ranked users by prestige and points.

* **/pokemon/shinysparkles** -- Meant to be a celebration when a shiny appears on screen.

* **/pokemon/currentbets** -- Lists all bets that have been made, and yet resolved

The following URLs show my shiny progress across various Pokemon games. Some are intended to be OBS overlays. Some aren't.

* **/pokemon/shinydex** -- Shows my current Shiny living dex completion rate across all games. 
    * **/pokemon/shinydex/hisui** -- Shows my Shiny dex progress in Pokemon Legends (overlay)
        * **/pokemon/shinydex/hisui/dex** -- Shows the same as above.  This is /hisui's default page
        * **/pokemon/shinydex/hisui/order** -- Shows all shiny catches registered in the bot.
        * **/pokemon/shinydex/hisui/missing** -- Shows the remaining Shiny pokemon I have yet to catch.
    * **/pokemon/shinydex/letsgo** -- Shows my Shiny progress in Let's Go Eevee (overlay)
    * **/pokemon/shinydex/go** -- Shows my Shiny progress in Pokemon Go

