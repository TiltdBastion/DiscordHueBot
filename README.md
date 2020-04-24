# Discord Hue Bot

A random Discord bot that give a server control to your hue bridge and thus your hue lights.

Did it while I was bored during covid quarantine.

# Usage

Fill the config.properties file in src/main/ressources with the token of your Discord bot, your Philips Hue username, the IP address of the bridge, the Discord UserId of the administator of the bot and the prefix that will be used.

## Philips Hue Username

Find the bridge IP address, you can use the command `curl https://discovery.meethue.com` to find it.

To obtain a username from your Philips Hue bridge, send a POST request to <http://IP_of_bridge/api/> with a JSON containing the devicetype in the body. Press the button before sending the request. You'll find your username in the response.

Or via curl: `curl -X POST -d '{"devicetype":"my_hue_app"}' IP_of_bridge/api`

## Discord Bot Token

Go to <https://discordapp.com/developers/applications> and create a new Application.

Then go to bot and click Add bot, you find the token. 

To invite the bot to your server, go to OAuth2, select the bot scope, then the wanted permissions (this bot only need to read and send messages to channels). Finally you can copy the given link to your browser to invite the bot to your server.