package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView hungerLevelTextView;
    private TextView happinessLevelTextView;
    private TextView coinsTextView;
    private int hungerLevel = 100;
    private int happinessLevel = 100;
    private int coins = 0;
    private Handler handler;
    private Runnable runnable;
    private List<Item> shopItems;
    private List<Item> inventoryItems;
    private boolean isPlayingMiniGame = false;
    private boolean isShopOpen = false;
    private boolean isInventoryOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hungerLevelTextView = findViewById(R.id.hunger_level_textview);
        happinessLevelTextView = findViewById(R.id.happiness_level_textview);
        coinsTextView = findViewById(R.id.coins_textview);

        updateHungerLevel();
        updateHappinessLevel();
        updateCoins();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                decreaseLevelsOverTime();
                handler.postDelayed(this, 120000); // Diminuer les jauges toutes les deux minutes (120000 ms)
            }
        };
        handler.postDelayed(runnable, 120000); // Lancer le processus de diminution des jauges

        Button feedButton = findViewById(R.id.feed_button);
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedAnimal();
            }
        });

        Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMiniGame();
            }
        });

        Button shopButton = findViewById(R.id.shop_button);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShop();
            }
        });

        Button inventoryButton = findViewById(R.id.inventory_button);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleInventory();
            }
        });

        // Initialiser la liste des éléments à vendre
        shopItems = new ArrayList<>();
        shopItems.add(new Item("Nourriture", 10));
        shopItems.add(new Item("Vêtement", 20));
        shopItems.add(new Item("Jouet", 15));
        shopItems.add(new Item("Accessoire", 30));
        shopItems.add(new Item("Medicament", 25));
        shopItems.add(new Item("Livre", 18));
        shopItems.add(new Item("FUSIL", 180));
        shopItems.add(new Item("BATEAU", 2000));
        shopItems.add(new Item("CUISINIERE", 160));
        shopItems.add(new Item("REFREGIRATEUR", 150));
        shopItems.add(new Item("ARMOIRE", 130));
        shopItems.add(new Item("CANAPE", 120));
        shopItems.add(new Item("TABLE", 100));
        shopItems.add(new Item("CHAISE", 80));
        shopItems.add(new Item("VOITURE", 1000));
        shopItems.add(new Item("IPHONE 12", 100));
        shopItems.add(new Item("TELEVISON SAMMSUNG", 180));
        shopItems.add(new Item("MACBOOK AIR", 139));
        shopItems.add(new Item("MACBOOK PRO", 150));
        shopItems.add(new Item("SACOCHE PRIMARK", 30));
        shopItems.add(new Item("SAC LV", 80));
        shopItems.add(new Item("SAC GUCCI", 70));

        // Initialiser la liste des objets dans l'inventaire
        inventoryItems = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêter le processus de diminution des jauges lorsque l'activité est détruite
        handler.removeCallbacks(runnable);
    }

    private void decreaseLevelsOverTime() {
        if (isPlayingMiniGame()) {
            return;
        }

        hungerLevel -= 5;
        happinessLevel -= 3;
        updateHungerLevel();
        updateHappinessLevel();
        checkLevels();
    }

    private boolean isPlayingMiniGame() {
        return isPlayingMiniGame;
    }

    private void updateHungerLevel() {
        hungerLevelTextView.setText("Faim : " + hungerLevel);
    }

    private void updateHappinessLevel() {
        happinessLevelTextView.setText("Bonheur : " + happinessLevel);
    }

    private void updateCoins() {
        coinsTextView.setText("Coins : " + coins);
    }

    private void checkLevels() {
        if (hungerLevel <= 0 || happinessLevel <= 0) {
            // Gérer le cas où une des jauges est à 0
            Toast.makeText(MainActivity.this, "Votre animal est malheureux. Prenez soin de lui !", Toast.LENGTH_SHORT).show();
        }
    }

    private void feedAnimal() {
        hungerLevel += 20;
        updateHungerLevel();
    }

    private void playMiniGame() {
        isPlayingMiniGame = true;

        // Exemple de système de mini-jeu
        Random random = new Random();
        int gameScore = random.nextInt(101); // Score obtenu dans le mini-jeu

        hungerLevel -= gameScore / 10;
        happinessLevel += gameScore / 5;
        coins += gameScore / 20;

        updateHungerLevel();
        updateHappinessLevel();
        updateCoins();

        isPlayingMiniGame = false;
    }

    private void toggleShop() {
        if (isShopOpen) {
            closeShop();
        } else {
            openShop();
        }
    }

    private void openShop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Shop");

        // Construire la liste des articles
        StringBuilder itemsString = new StringBuilder();
        for (int i = 0; i < shopItems.size(); i++) {
            Item item = shopItems.get(i);
            itemsString.append(i + 1).append(". ").append(item.getName()).append(" - ").append(item.getPrice()).append(" coins\n");
        }

        // Afficher la liste des articles dans la boîte de dialogue
        builder.setMessage(itemsString.toString());

        builder.setPositiveButton("Acheter", (dialog, which) -> {
            // Logique pour acheter l'article sélectionné
            buyItem(shopItems.get(which));
        });

        builder.setNegativeButton("Fermer", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        isShopOpen = true;
        isInventoryOpen = false;
    }

    private void closeShop() {
        // Logique pour fermer la vue du shop
        isShopOpen = false;
    }

    private void toggleInventory() {
        if (isInventoryOpen) {
            closeInventory();
        } else {
            openInventory();
        }
    }

    private void openInventory() {
        // Afficher les éléments dans l'inventaire
        StringBuilder itemsString = new StringBuilder();
        for (Item item : inventoryItems) {
            itemsString.append(item.getName()).append("\n");
        }
        String inventoryMessage = "Voici les éléments dans votre inventaire :\n\n" + itemsString.toString();
        Toast.makeText(MainActivity.this, inventoryMessage, Toast.LENGTH_LONG).show();

        isInventoryOpen = true;
        isShopOpen = false;
    }

    private void closeInventory() {
        // Logique pour fermer la vue de l'inventaire
        isInventoryOpen = false;
    }

    private void buyItem(Item item) {
        if (coins >= item.getPrice()) {
            coins -= item.getPrice();
            inventoryItems.add(item);
            updateCoins();
            Toast.makeText(MainActivity.this, "Achat effectué : " + item.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Vous n'avez pas assez de coins pour acheter cet élément.", Toast.LENGTH_SHORT).show();
        }
    }

    // Classe Item pour représenter un élément à vendre
    private class Item {
        private String name;
        private int price;

        public Item(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }
    }
}
