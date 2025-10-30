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
    private ArrayList<Item> inventory;
    private int playerHealth;
    private int maxHealth;
    private int playerDamage;
    private int playerShield;
    private int playerHeal;
    private int playerSpeed;
    private boolean shieldup;
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
        gui = new MonsterBattleGUI("Monster Battle - MY GAME");
        int numMonsters = chooseDifficulty();
        
        // TODO: Setup player health
        maxHealth = 100;  // Change this if you want
        playerHealth = 100;
        playerDamage = 125;
        gui.setPlayerMaxHealth(maxHealth);
        gui.updatePlayerHealth(playerHealth);
        
        monsters = new ArrayList<>();
        for(int k = 0; k < numMonsters; k++) monsters.add(new Monster());
        gui.updateMonsters(monsters);
        
        // TODO: Create starting items
        inventory = new ArrayList<>();
        // Add items here! Look at GameDemo.java for examples
        gui.updateInventory(inventory);
        
        // TODO: Customize button labels
        String[] buttons = {"Attack (" + playerDamage + ")",
                            "Defend (" + playerShield + ")",
                            "Heal (" + playerHeal + ")",
                            "Use Item"};
        gui.setActionButtons(buttons);
        
        // Welcome message
        gui.displayMessage("Welcome to Owen's Monster Game!");
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
            shieldup = false;

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
     * Handle player's action choice
     * 
     * TODO: What happens for each action?
     */
        /**
     * Let player choose difficulty (number of monsters) using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private int chooseDifficulty() {
        // Set button labels to difficulty levels
        String[] difficulties = {"Easy (2)", "Medium (3)", "Hard (4)", "Extreme (5)"};
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
        // Determine number of monsters based on choice
        //int numMonsters = 2 + choice;  // 2, 3, 4, or 5 monsters
        
        gui.displayMessage("You will face " + numMonsters +  " monsters!");
        gui.pause(1500);
        
        return numMonsters;
    }        
    
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
            case 3: // Use Item button
                useItem();
                break;
        }
    }
    
    /**
     * Attack a monster
     * 
     * TODO: How does attacking work in your game?
     * - How much damage?
     * - Which monster gets hit?
     * - Special effects?
     */
    private void attackMonster() {
        // TODO: Implement your attack!
        Monster target = lowestHealthMonster();
        int baseDamage = (int)(playerDamage * 0.15);
        int damage = baseDamage + (int)(Math.random() * baseDamage);
        target.takeDamage(damage);
        int index = monsters.indexOf(target);
        gui.highlightMonster(index);
        gui.pause(670);
        
        // Hint: Look at GameDemo.java for an example
        
        gui.displayMessage("You dealt for " + damage + " damage!");
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
        // TODO: Implement your defend!
        shieldup = true;
        
        gui.displayMessage("TODO: Implement defend!");
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
        int min = 10;
        int max = 40;
        int heal = (int)(Math.random() * max - min + 1) + min;
        playerHealth += heal;
        gui.displayMessage("You healed for " + heal + "HP!");
        gui.updatePlayerHealth(playerHealth);
    }
    
    /**
     * Use an item from inventory
     */
    private void useItem() {
        if (inventory.isEmpty()) {
            gui.displayMessage("No items in inventory!");
            return;
        }
        
        // Use first item
        Item item = inventory.remove(0);
        gui.updateInventory(inventory);
        item.use();  // The item knows what to do!
    }
    
    /**
     * Monster attacks player
     * 
     * TODO: Customize how monsters attack!
     * - How much damage?
     * - Which monster attacks?
     * - Special abilities?
     */
    private void monsterAttack() {
        // TODO: Implement monster attacks!
        double monsterdamage = 0;
        int minMonsterSpeed = 0;

        for(Monster m : monsters){
            if (m.speed() > minMonsterSpeed) monsterdamage = m.damage();
        }

        // Hint: Look at GameDemo.java for an example
        playerHealth -= monsterdamage;
        gui.displayMessage("Monster dealt " + monsterdamage + " damage.");
        gui.updatePlayerHealth(playerHealth);
    }
    
    // ==================== HELPER METHODS ====================
    // Add your own helper methods here!
    
    /**
     * Count how many monsters are still alive
     */
    private Monster lowestHealthMonster(){
        int lowest = 100;
        Monster monsternum = null;
        for (Monster m : monsters){
            if (m.health() < lowest && m.health() > 0) monsternum = m;
        }
        return monsternum;
    }

    private int countLivingMonsters() {
        int count = 0;
        for (Monster m : monsters) {
            if (m.health() > 0) count++;
        }
        return count;
    }
    
    //Returns ArrayList of monsters with specials
    private ArrayList<Monster> getSpecialMonsters(){
        ArrayList<Monster> specialMonsters = new ArrayList<>();
        for(Monster m : monsters){
            if(m.special() != null && !m.special().equals("") && m.health() > 0) specialMonsters.add(m);
        }
        return specialMonsters;
    }

    //Returns ArrayList of monsters with a speed greater than the player
    private ArrayList<Monster> getSpeedyMonsters(){
        ArrayList<Monster> speedyMonsters = new ArrayList<>();
        for(Monster m : monsters){
            if(m.speed() > playerSpeed && m.health() > 0) speedyMonsters.add(m);
        }
        return speedyMonsters;
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
}