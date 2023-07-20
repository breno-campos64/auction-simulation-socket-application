# Auction Simulation Socket Application
- College group project on socket communication
- Aims to simulate a socket based auction application

## Previews
![preview1](https://github.com/breno-campos64/auction-simulation-socket-application/blob/main/assets/preview_1.png)

![preview2](https://github.com/breno-campos64/auction-simulation-socket-application/blob/main/assets/preview_2.png)

## Technologies
- Java

## How to run
- Open cmd, then type ipconfig
- Copy the IPv4 address
- Open the "ClienteComprador.java" file, then replace "localhost" with the copied IPv4 address
- Open the "ClienteVendedor.java" file, then replace "localhost" with the copied IPv4 address
- Open cmd inside the project folder, then type "java Servidor" (The server will run)
- While the server is running, you cam open more terminals to act as the users
- Type "java ClienteVendedor", then type "1" to put up a product up for auction
- Insert the product name, description and initial price
- After you're done, type "3" to leave
- Now, type "java ClienteComprador", then type "1" to see the list of products up for auction
- You will see thet product you created as the seller in the list
- Type "2" to make a bid
- Enter the product name, your bid and a fake email
- Type "3" to leave
- Type "java ClienteVendedor" to enter as the seller again
- Type "2" to end an auction
- Enter the product name you put up for auction earlier
- Finally, you will see who won the auction and their bid (which will be the buyer you entered as earlier)
