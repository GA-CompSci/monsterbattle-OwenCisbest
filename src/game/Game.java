package game;
import gui.MonsterBattleGUI;
import java.util.ArrayList;

/**
 * Game - YOUR monster battle game!
 * 
 * Build your game here. Look at GameDemo.java for examples.
 * 
 * Steps:
 * 1. Fill in setupGame() - create monsters, items, set health
 * 2. Fill in the action methods - what happens when player acts?
 * 3. Customize the game loop if you want
 * 4. Add your own helper methods
 * 
 * Run this file to play YOUR game
 */
public class Game {
    
    // The GUI (I had AI build most of this)
    private MonsterBattleGUI gui;
    
    // Game state - YOU manage these
    private ArrayList<Monster> monsters;
    private Monster lastAttacked; // store the monster we last attacked so it can respond
    private int shieldPower = 0;
    private ArrayList<Item> inventory;
    private int playerHealth;
    private int playerSpeed;
    private int playerDamage;
    private int playerHeal;    
    private int playerShield;
    private String playerClass = "";
    private int specialCooldown = 0;
    
    /**
     * Main method - start YOUR game!
     */
    public static void main(String[] args) {
        Game game = new Game(); // it instantiates a copy of this file. We're not running static
        game.play(); // this extra step is unnecessary AI stuff
    }
    
    /**
     * Play the game!
     */
    public void play() {
        setupGame();
        gameLoop();
    }
    
    /**
     * Setup - create the GUI and initial game state
     * 
     * TODO: Customize this! How many monsters? What items? How much health?
     */
    private void setupGame() {
        // Create the GUI
        gui = new MonsterBattleGUI("Monster Battle - Owen C.");

        // CHOOSE DIFFICULTY (number of monsters to face)
        int numMonsters = chooseDifficulty();
        monsters = new ArrayList<>();
        // Should we add special abilities? 
        for (int k = 0; k < numMonsters; k++) {
            double num = randomize();
            if (num > 0.90) {
                monsters.add(new Monster("Vampire"));
            }
            else if(num < 0.03){
                monsters.add(new Monster("Mason 67 Mango"));
            }
            else if(num < 0.07){
                monsters.add(new Monster("Bomber"));
            }
            else monsters.add(new Monster());
        }
        gui.updateMonsters(monsters);  

        // PICK YOUR CHARACTER BUILD (using the 4 action buttons!)
        pickCharacterBuild(); 


        // TODO: Create starting items
        inventory = new ArrayList<>();
        // Add items here! Look at GameDemo.java for examples
        gui.updateInventory(inventory);
        
        // ACTION BUTTONS
        String[] buttons = {"Attack (" + playerDamage + ")", 
                            "Defend (" + playerShield + ")", 
                            "Heal (" + playerHeal + ")", 
                            "Use Item"};
        gui.setActionButtons(buttons);
        
        // Welcome message
        gui.displayMessage("Battle Start! Make your move.");
    }
    
    /**
     * Main game loop
     * 
     * This controls the flow: player turn → monster turn → check game over
     * You can modify this if you want!
     */
    private void gameLoop() {
        // Keep playing while monsters alive and player alive
        while (countLivingMonsters() > 0 && playerHealth > 0) {
            shieldPower = 0; // start of turn, lower shield 
    
            // PLAYER'S TURN
            gui.displayMessage("Your turn! HP: " + playerHealth);
            int action = gui.waitForAction();  // Wait for button click (0-3)
            handlePlayerAction(action);
            gui.updateMonsters(monsters);
            gui.pause(500);
            
            // MONSTER'S TURN (if any alive and player alive)
            if (countLivingMonsters() > 0 && playerHealth > 0) {
                monsterAttack();
                gui.updateMonsters(monsters);
                gui.pause(500);
                if (specialCooldown > 0) specialCooldown--;
            }
        }
        
        // Game over!
        if (playerHealth <= 0) {
            gui.displayMessage("💀 DEFEAT! You have been defeated...");
        } else {
            gui.displayMessage("🎉 VICTORY! You defeated all monsters!");
        }
    }
    
    /**
     * Let player choose difficulty (number of monsters) using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private int chooseDifficulty() {
        // Set button labels to difficulty levels
        String[] difficulties = {"Easy (2-4)", "Medium (4-5)", "Hard (6-8)", "Extreme (10-15)"};
        gui.setActionButtons(difficulties);
        
        // Display choice prompt
        gui.displayMessage("---- CHOOSE DIFFICULTY ----");
        
        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();
        int numMonsters = 0;
        switch(choice){
            case 0:
                numMonsters = (int)(Math.random() * (4-2+1)) + 2;
                break;
            case 1: 
                numMonsters = (int)(Math.random() * (5-4+1)) + 4;
                break;
            case 2:
                numMonsters = (int)(Math.random() * (8-6+1)) + 6;
                break;
            case 3:
                numMonsters = (int)(Math.random() * (15-10+1)) + 10;
                break;
        }
        
        gui.displayMessage("You will face " + numMonsters + " monsters! Good luck!");
        gui.pause(1500);
        
        return numMonsters;
    }
    
        /**
     * Let player pick their character build using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private void pickCharacterBuild() {
        // Set button labels to character classes
        String[] characterClasses = {"Fighter", "Tank", "Healer", "Ninja"};
        gui.setActionButtons(characterClasses);
        
        // Display choice prompt
        gui.displayMessage("---- PICK YOUR BUILD ----");
        
        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();
        
        // Initialize default stats
        playerDamage = 50;
        playerShield = 50;
        playerHeal = 20;
        playerSpeed = 10;
        playerHealth = 100;
        
        // Customize stats based on character choice
        if (choice == 0) {
            playerClass = "Fighter";
            // Fighter: high damage, low healing and shield
            gui.displayMessage("You chose Fighter! High damage, but weak defense.");
            playerShield -= (int)(Math.random() * 20 + 1) + 5;  // Reduce shield by 5-25
            playerHeal -= (int)(Math.random() * 20 + 1) + 5;        // Reduce heal by 5-25
        } else if (choice == 1) {
            playerClass = "Tank";
            // Tank: high shield, low damage and speed
            gui.displayMessage("You chose Tank! Tough defense, but slow attacks.");
            playerSpeed -= (int)(Math.random() * 9) + 1;        // Reduce speed by 1-9
            playerDamage -= (int)(Math.random() * 20 + 1) + 5;   // Reduce damage by 5-25
        } else if (choice == 2) {
            playerClass = "Healer";
            // Healer: high healing, low damage and shield
            gui.displayMessage("You chose Healer! Great recovery, but fragile.");
            playerDamage -= (int)(Math.random() * 21) + 5;      // Reduce damage by 5-25
            playerShield -= (int)(Math.random() * 21) + 5;      // Reduce shield by 5-25
        } else {
            playerClass = "Ninja";
            // Ninja: high speed, low healing and health
            gui.displayMessage("You chose Ninja! Fast and deadly, but risky.");
            playerHeal -= (int)(Math.random() * 21) + 5;        // Reduce heal by 5-25
            playerHealth -= (int)(Math.random() * 21) + 5;         // Reduce max health by 5-25
        }

        gui.setPlayerMaxHealth(playerHealth);
        gui.updatePlayerHealth(playerHealth);
        
        // Pause to let player see their choice
        gui.pause(1500);
    }
    
    /**
     * Handle player's action choice
     * 
     * TODO: What happens for each action?
     */
    private void handlePlayerAction(int action) {
        switch (action) {
            case 0: // Attack button
                attackMonster();
                break;
            case 1: // Defend button
                defend();
                break;
            case 2: // Heal button
                heal();
                break;
            case 3: // Special ability button
                useSpecial();
                break;
        }
    }
    
    /**
     * Attack a monster
     * 
     * - How much damage?
     * - Which monster gets hit?
     * - Special effects?
     */
    private void attackMonster() {
        // TODO: Target more intelligently
        Monster target = getRandomLivingMonster();
        lastAttacked = target;
        int damage = (int)(Math.random() * playerDamage + 1); // 0 - playerDamage
        if(damage == 0) {
            // hurt yourself
            playerHealth -= 5;
            gui.displayMessage("Critical fail! You hit yourself for 5 points");
            gui.updatePlayerHealth(playerHealth);
        } else if(damage == playerDamage){
            gui.displayMessage("Critical hit! You slayed the monster");
            target.takeDamage(target.health());
        } else {
           target.takeDamage(damage); 
           gui.displayMessage("You hit the monster for " + damage + " damage");
        }
        // Show which one we hit
        int index = monsters.indexOf(target);
        gui.highlightMonster(index);
        gui.pause(300);
        gui.highlightMonster(-1);
        // update the list
        gui.updateMonsters(monsters);
    }
    
    /**
     * Defend
     * 
     * TODO: What does defending do?
     * - Reduce damage?
     * - Block next attack?
     * - Something else?
     */
    private void defend() {
        shieldPower = playerShield; // charge up my shield
        
        gui.displayMessage("Shield up! Brace for impact!");
    }
    
    /**
     * Heal yourself
     * 
     * TODO: How does healing work?
     * - How much HP?
     * - Any limits?
     */
    private void heal() {
        // TODO: Implement your heal!
        playerHealth += playerHeal;
        gui.updatePlayerHealth(playerHealth);
        gui.displayMessage("You healed for " + playerHeal + " health.");
    }
    
    /**
     * Use class special ability if cooldown is at 0
     */
    private void useSpecial() {
        if (specialCooldown > 0) {
            gui.displayMessage("Special on cooldown for " + specialCooldown + " more turns.");
            return;
        }
        
        switch(playerClass) {
            case "Fighter":
                Monster target = getRandomLivingMonster();
                if (target != null && target.health() > 0) {
                    int dmg = playerDamage * 2;
                    target.takeDamage(dmg);
                    gui.displayMessage("Power Strike! You dealt " + dmg + " extra damage!");
                    specialCooldown = 3;
                }
                break;
            case "Tank":
                shieldPower = playerShield * 3;
                gui.displayMessage("Fortify! Shield strength now at " + shieldPower);
                specialCooldown = 4;
                break;
            case "Healer":
                playerHealth += playerHeal * 3;
                if (playerHealth > 100) playerHealth = 100; // TODO: use actual max. Idk if imma actually change this later, just cuz im lazy.
                gui.displayMessage("Mega Heal! You just healed for " + (playerHeal * 3) + " health!");
                gui.updatePlayerHealth(playerHealth);
                specialCooldown = 3;
                break;
            case "Ninja":
                Monster specialtarget = getRandomLivingMonster();
                if (specialtarget != null) {
                    int specialdamage = (int)(playerDamage * 1.5);
                    specialtarget.takeDamage(specialdamage);
                    gui.displayMessage("Ambush! " + specialdamage + " damage!");
                    specialCooldown = 2;
                }
                break;
        }
    }
    
    /**
     * Monster attacks player
     * - How much damage?
     * - Which monster attacks?
     * - Special abilities?
     */
    private void monsterAttack() {
        // build a list of every monster that gets to take a swipe at us
        ArrayList<Monster> attackers = getSpeedyMonsters();
        // first check if there is a lastAttacked
        if(lastAttacked != null && lastAttacked.health() > 0 && !attackers.contains(lastAttacked)) 
            attackers.add(lastAttacked);

        for (Monster monster : attackers) {
            // shoudn't the monster's damage dealt logic be handle in the Monster class? 
            int damageTaken = (int)(Math.random() * monster.damage() + 1);
            if (shieldPower > 0) {
                double absorbance = Math.min(damageTaken, shieldPower);
                damageTaken -= absorbance;
                shieldPower -= absorbance;
                gui.displayMessage("You block for " + absorbance + " damage. You have " + shieldPower + " shield left.");
            }
            if(monster.special().equals("Vampire")){
                monster.takeDamage(-damageTaken/5);
                gui.displayMessage("The vampire uses some of your damage to heal itself for " + damageTaken/5 + " health!");            
            }
            else if(monster.special().equals("Mason 67 Mango")){
                double chance = randomize();
                if(chance < 0.02){
                    double doublechance = randomize();
                    if(doublechance > 0.98){
                        monster.takeDamage(100000);
                        gui.displayMessage("whoopsies, it appears mason 67 mango monster has been obliterated");                        
                    }
                }


            }
            else if(monster.special().equals("Bomber")){
                damageTaken += 15;
                monster.takeDamage(monster.health());
                gui.displayMessage("It appears the bomber has exploded, killing itself, but dealing an extra 15 damage!");            

            }          
            if (damageTaken > 0) {
                playerHealth -= damageTaken;
                gui.displayMessage("Monster hits you for " + damageTaken + " damage!");
                gui.updatePlayerHealth(playerHealth);

            }
            //else if(monster.special().equals("Undead medic")){
              //  monster
           // }
            int index = monsters.indexOf(monster);
            gui.highlightMonster(index);
            gui.pause(300);
            gui.highlightMonster(-1);
        }

    }
    
    // ==================== HELPER METHODS ====================
    // Add your own helper methods here!
    
    /**
     * Count how many monsters are still alive
     */
    private ArrayList<Monster> monstersAboveHealth(ArrayList<Monster> monsters){
        ArrayList<Monster> monstersabove = new ArrayList<>();
        for(int i = 0; i <= monsters.size(); i++){
            if(monsters.get(i).health() > 10) monstersabove.add(monsters.get(i));
        }
        return monstersabove;
    }
    private int countLivingMonsters() {
        int count = 0;
        for (Monster m : monsters) {
            if (m.health() > 0) count++;
        }
        return count;
    }

    private ArrayList<Monster> getSpecialMonsters() {
        ArrayList<Monster> result = new ArrayList<>();
        for(Monster m : monsters){
            if(m.special() != null && !m.special().equals("") && m.health() > 0){
                result.add(m);
            }
        }
        return result;
    }

    private ArrayList<Monster> getSpeedyMonsters() {
        ArrayList<Monster> result = new ArrayList<>();
        for(Monster m : monsters){
            if(m.speed() > playerSpeed && m.health() > 0){
                result.add(m);
            }
        }
        return result;
    }
    
    /**
     * Get a random living monster
     */
    private Monster getRandomLivingMonster() {
        ArrayList<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.health() > 0) alive.add(m);
        }
        if (alive.isEmpty()) return null;
        return alive.get((int)(Math.random() * alive.size()));
    }
    
    // TODO: Add more helper methods as you need them!
    // Examples:
    // - Method to find the strongest monster
    // - Method to check if player has a specific item
    // - Method to add special effects
    // - etc.
    public static double randomize(){
        double num = Math.random();
        return num;
    }
}