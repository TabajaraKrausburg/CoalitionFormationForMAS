package massim.scenario.city.util;

import massim.protocol.scenario.city.data.JobData;
import massim.scenario.city.data.*;
import massim.scenario.city.data.facilities.*;
import massim.util.Log;
import massim.util.RNG;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility to generate random elements with.
 */
public class Generator {

    private double quadSize;
    private double blackoutProbability;
    private int blackoutTimeMin;
    private int blackoutTimeMax;

    private double chargingDensity;
    private int rateMin;
    private int rateMax;

    private double shopDensity;
    private int minProd;
    private int maxProd;
    private int amountMin;
    private int amountMax;
    private int priceAddMin;
    private int priceAddMax;
    private int restockMin;
    private int restockMax;

    private double dumpDensity;

    private double workshopDensity;

    private double storageDensity;
    private int capacityMin;
    private int capacityMax;

    private double resourceDensity;
    private int gatherFrequencyMin;
    private int gatherFrequencyMax;

    private int baseItemsMin;
    private int baseItemsMax;
    private int levelDecreaseMin;
    private int levelDecreaseMax;
    private int graphDepthMin;
    private int graphDepthMax;
    private int resourcesMin;
    private int resourcesMax;

    private int minVol;
    private int maxVol;
    private int valueMin;
    private int valueMax;
    private int minReq;
    private int maxReq ;
    private int reqAmountMin;
    private int reqAmountMax;

    private int toolsMin;
    private int toolsMax;
    private double toolProbability;

    private double rate;
    private double auctionProbability;
    private double missionProbability;
    private int productTypesMin;
    private int productTypesMax;
    private int difficultyMin;
    private int difficultyMax;
    private int timeMin;
    private int timeMax;
    private int rewardAddMin;
    private int rewardAddMax;

    private int auctionTimeMin;
    private int auctionTimeMax;
    private int fineSub;
    private int fineAdd;
    private int maxRewardAdd;

    private int missionDifficultyMax;

    private int maxCapacity;

    private List<Item> baseItems = new ArrayList<>();
    private List<List<Item>> itemGraph = new ArrayList<>();
    private List<Item> resources = new ArrayList<>();
    private List<Tool> allTools = new ArrayList<>();
    private List<Item> assembledItems = new ArrayList<>();
    private int missionID = 0;
    private int missionEnd = 0;

    private Set<Facility> blackoutFacilities = new HashSet<>();

    public Generator(JSONObject randomConf){
        //parse facilities
        JSONObject facilities = randomConf.optJSONObject("facilities");
        if(facilities == null) {
            Log.log(Log.Level.ERROR, "No facilities in configuration.");
        } else {
            quadSize = facilities.optDouble("quadSize", 0.4);
            Log.log(Log.Level.NORMAL, "Configuring facilities quadSize: " + quadSize);
            blackoutProbability = facilities.optDouble("blackoutProbability", 0.1);
            Log.log(Log.Level.NORMAL, "Configuring facilities blackoutProbability: " + blackoutProbability);
            blackoutTimeMin = facilities.optInt("blackoutTimeMin", 5);
            Log.log(Log.Level.NORMAL, "Configuring facilities blackoutTimeMin: " + blackoutTimeMin);
            blackoutTimeMax = facilities.optInt("blackoutTimeMax", 10);
            Log.log(Log.Level.NORMAL, "Configuring facilities blackoutTimeMax: " + blackoutTimeMax);

            //parse charging stations
            JSONObject chargingStations = facilities.optJSONObject("chargingStations");
            if (chargingStations == null) {
                Log.log(Log.Level.ERROR, "No charging stations in configuration.");
            } else {
                chargingDensity = chargingStations.optDouble("density", 0.9);
                Log.log(Log.Level.NORMAL, "Configuring facilities charging station density: " + chargingDensity);
                rateMin = chargingStations.optInt("rateMin", 50);
                Log.log(Log.Level.NORMAL, "Configuring facilities charging station rateMin: " + rateMin);
                rateMax = chargingStations.optInt("rateMax", 150);
                Log.log(Log.Level.NORMAL, "Configuring facilities charging station rateMax: " + rateMax);
            }

            //parse shops
            JSONObject shops = facilities.optJSONObject("shops");
            if (shops == null) {
                Log.log(Log.Level.ERROR, "No shops in configuration.");
            } else {
                shopDensity = shops.optDouble("density", 0.8);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop density: " + shopDensity);
                minProd = shops.optInt("minProd", 3);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop minProd: " + minProd);
                maxProd = shops.optInt("maxProd", 10);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop maxProd: " + maxProd);
                amountMin = shops.optInt("amountMin", 5);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop amountMin: " + amountMin);
                amountMax = shops.optInt("amountMax", 20);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop amountMax: " + amountMax);
                priceAddMin = shops.optInt("priceAddMin", 100);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop priceAddMin: " + priceAddMin);
                priceAddMax = shops.optInt("priceAddMax", 150);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop priceAddMax: " + priceAddMax);
                restockMin = shops.optInt("restockMin", 1);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop restockMin: " + restockMin);
                restockMax = shops.optInt("restockMax", 5);
                Log.log(Log.Level.NORMAL, "Configuring facilities shop restockMax: " + restockMax);
            }

            //parse dumps
            JSONObject dumps = facilities.optJSONObject("dumps");
            if (dumps == null) {
                Log.log(Log.Level.ERROR, "No dumps in configuration.");
            } else {
                dumpDensity = dumps.optDouble("density", 0.6);
                Log.log(Log.Level.NORMAL, "Configuring facilities dump density: " + dumpDensity);
            }

            //parse workshops
            JSONObject workshops = facilities.optJSONObject("workshops");
            if (workshops == null) {
                Log.log(Log.Level.ERROR, "No workshops in configuration.");
            } else {
                workshopDensity = workshops.optDouble("density", 0.6);
                Log.log(Log.Level.NORMAL, "Configuring facilities workshops density: " + workshopDensity);
            }

            //parse storage
            JSONObject storage = facilities.optJSONObject("storage");
            if (storage == null) {
                Log.log(Log.Level.ERROR, "No storage in configuration.");
            } else {
                storageDensity = storage.optDouble("density", 0.8);
                Log.log(Log.Level.NORMAL, "Configuring facilities storage density: " + storageDensity);
                capacityMin = storage.optInt("capacityMin", 7000);
                Log.log(Log.Level.NORMAL, "Configuring facilities storage capacityMin: " + capacityMin);
                capacityMax = storage.optInt("capacityMax", 10000);
                Log.log(Log.Level.NORMAL, "Configuring facilities storage capacityMax: " + capacityMax);
            }

            //parse resourceNodes
            JSONObject resourceNodes = facilities.optJSONObject("resourceNodes");
            if (resourceNodes == null) {
                Log.log(Log.Level.ERROR, "No resource nodes in configuration.");
            } else {
                resourceDensity = resourceNodes.optDouble("density", 0.7);
                Log.log(Log.Level.NORMAL, "Configuring facilities resource node density: " + resourceDensity);
                gatherFrequencyMin = resourceNodes.optInt("gatherFrequencyMin", 4);
                Log.log(Log.Level.NORMAL, "Configuring facilities resource node gatherFrequencyMin: " + gatherFrequencyMin);
                gatherFrequencyMax = resourceNodes.optInt("gatherFrequencyMax", 8);
                Log.log(Log.Level.NORMAL, "Configuring facilities resource node gatherFrequencyMax: " + gatherFrequencyMax);
            }
        }

        //parse items
        JSONObject items = randomConf.optJSONObject("items");
        if(items == null) {
            Log.log(Log.Level.ERROR, "No items in configuration.");
        } else {
            baseItemsMin = items.optInt("baseItemsMin",5);
            Log.log(Log.Level.NORMAL, "Configuring items baseItemsMin: " + baseItemsMin);
            baseItemsMax = items.optInt("baseItemsMax",7);
            Log.log(Log.Level.NORMAL, "Configuring items baseItemsMax: " + baseItemsMax);
            levelDecreaseMin = items.optInt("levelDecreaseMin",1);
            Log.log(Log.Level.NORMAL, "Configuring items levelDecreaseMin: " + levelDecreaseMin);
            levelDecreaseMax = items.optInt("levelDecreaseMax",2);
            Log.log(Log.Level.NORMAL, "Configuring items levelDecreaseMax: " + levelDecreaseMax);
            graphDepthMin = items.optInt("graphDepthMin",3);
            Log.log(Log.Level.NORMAL, "Configuring items graphDepthMin: " + graphDepthMin);
            graphDepthMax = items.optInt("graphDepthMax",4);
            Log.log(Log.Level.NORMAL, "Configuring items graphDepthMax: " + graphDepthMax);
            resourcesMin = items.optInt("resourcesMin",1);
            Log.log(Log.Level.NORMAL, "Configuring items resourcesMin: " + resourcesMin);
            resourcesMax = items.optInt("resourcesMax",1);
            Log.log(Log.Level.NORMAL, "Configuring items resourcesMax: " + resourcesMax);

            minVol = items.optInt("minVol",10);
            Log.log(Log.Level.NORMAL, "Configuring items minVol: " + minVol);
            maxVol = items.optInt("maxVol",100);
            Log.log(Log.Level.NORMAL, "Configuring items maxVol: " + maxVol);
            valueMin = items.optInt("valueMin",10);
            Log.log(Log.Level.NORMAL, "Configuring items valueMin: " + valueMin);
            valueMax = items.optInt("valueMax",100);
            Log.log(Log.Level.NORMAL, "Configuring items valueMax: " + valueMax);
            minReq = items.optInt("minReq",1);
            Log.log(Log.Level.NORMAL, "Configuring items minReq: " + minReq);
            maxReq = items.optInt("maxReq",3);
            Log.log(Log.Level.NORMAL, "Configuring items maxReq: " + maxReq);
            reqAmountMin = items.optInt("reqAmountMin",1);
            Log.log(Log.Level.NORMAL, "Configuring items reqAmountMin: " + reqAmountMin);
            reqAmountMax = items.optInt("reqAmountMax",3);
            Log.log(Log.Level.NORMAL, "Configuring items reqAmountMax: " + reqAmountMax);

            toolsMin = items.optInt("toolsMin",1);
            Log.log(Log.Level.NORMAL, "Configuring items toolsMin: " + toolsMin);
            toolsMax = items.optInt("toolsMax",1);
            Log.log(Log.Level.NORMAL, "Configuring items toolsMax: " + toolsMax);
            toolProbability = items.optDouble("toolProbability",0.5);
            Log.log(Log.Level.NORMAL, "Configuring items toolProbability: " + toolProbability);
        }

        //parse jobs
        JSONObject jobs = randomConf.optJSONObject("jobs");
        if(jobs == null) {
            Log.log(Log.Level.ERROR, "No jobs in configuration.");
        } else {
            rate = jobs.optDouble("rate", 0.2);
            Log.log(Log.Level.NORMAL, "Configuring jobs rate: " + rate);
            auctionProbability = jobs.optDouble("auctionProbability", 0.4);
            Log.log(Log.Level.NORMAL, "Configuring jobs auctionProbability: " + auctionProbability);
            missionProbability = jobs.optDouble("missionProbability", 0.1);
            Log.log(Log.Level.NORMAL, "Configuring jobs missionProbability: " + missionProbability);
            productTypesMin = jobs.optInt("productTypesMin", 1);
            Log.log(Log.Level.NORMAL, "Configuring jobs productTypesMin: " + productTypesMin);
            productTypesMax = jobs.optInt("productTypesMax", 4);
            Log.log(Log.Level.NORMAL, "Configuring jobs productTypesMax: " + productTypesMax);
            difficultyMin = jobs.optInt("difficultyMin", 3);
            Log.log(Log.Level.NORMAL, "Configuring jobs difficultyMin: " + difficultyMin);
            difficultyMax = jobs.optInt("difficultyMax", 12);
            Log.log(Log.Level.NORMAL, "Configuring jobs difficultyMax: " + difficultyMax);
            timeMin = jobs.optInt("timeMin", 100);
            Log.log(Log.Level.NORMAL, "Configuring jobs timeMin: " + timeMin);
            timeMax = jobs.optInt("timeMax", 400);
            Log.log(Log.Level.NORMAL, "Configuring jobs timeMax: " + timeMax);
            rewardAddMin = jobs.optInt("rewardAddMin", 50);
            Log.log(Log.Level.NORMAL, "Configuring jobs rewardAddMin: " + rewardAddMin);
            rewardAddMax = jobs.optInt("rewardAddMax", 100);
            Log.log(Log.Level.NORMAL, "Configuring jobs rewardAddMax: " + rewardAddMax);


            //parse auctions
            JSONObject auctions = jobs.optJSONObject("auctions");
            if (auctions == null) {
                Log.log(Log.Level.ERROR, "No auctions in configuration.");
            } else {
                auctionTimeMin = auctions.optInt("auctionTimeMin", 2);
                Log.log(Log.Level.NORMAL, "Configuring jobs auctionTimeMin: " + auctionTimeMin);
                auctionTimeMax = auctions.optInt("auctionTimeMax", 10);
                Log.log(Log.Level.NORMAL, "Configuring jobs auctionTimeMax: " + auctionTimeMax);
                fineSub = auctions.optInt("fineSub", 50);
                Log.log(Log.Level.NORMAL, "Configuring jobs fineSub: " + fineSub);
                fineAdd = auctions.optInt("fineAdd", 50);
                Log.log(Log.Level.NORMAL, "Configuring jobs fineAdd: " + fineAdd);
                maxRewardAdd = auctions.optInt("maxRewardAdd", 50);
                Log.log(Log.Level.NORMAL, "Configuring jobs maxRewardAdd: " + maxRewardAdd);
            }

            //parse missions
            JSONObject missions = jobs.optJSONObject("missions");
            if (missions == null) {
                Log.log(Log.Level.ERROR, "No missions in configuration.");
            } else {
                missionDifficultyMax = missions.optInt("missionDifficultyMax", 2);
                Log.log(Log.Level.NORMAL, "Configuring jobs missionDifficultyMax: " + missionDifficultyMax);
            }
        }
    }

    /**
     * Generates a number of tools dependent on config parameters
     * @param roleMap map from role names to roles
     * @return a list of tools
     */
    public List<Tool> generateTools(Map<String, Role> roleMap){
        int toolAmount = RNG.nextInt(toolsMax - toolsMin + 1) + toolsMin;
        List<Role> roles = new ArrayList<>(roleMap.values());

        //find role with maximum capacity
        Role maxRole = roles.stream().max(Comparator.comparingInt(Role::getMaxLoad)).orElse(roles.get(0));
        maxCapacity = maxRole.getMaxLoad();

        for(int i = 0; i < toolAmount; i++){
            String name = "tool" + i;
            int volume = RNG.nextInt(maxVol - minVol + 1) + minVol;
            int value = RNG.nextInt(valueMax - valueMin + 1) + valueMin;
            Set<String> toolRoles = new HashSet<>();

            // add one role at random
            int randomRole = RNG.nextInt(roles.size());
            if(roles.get(randomRole).getMaxLoad() > volume) toolRoles.add(roles.get(randomRole).getName());
            else{
                // if volume too high, set it low enough and give to role with max capacity
                if(volume > maxCapacity) volume = (int) (maxCapacity * 0.9);
                toolRoles.add(maxRole.getName());
            }
//          if(RNG.nextInt(2) < 1){
            if(RNG.nextInt(1) < 1){
                // add a second role at 50% chance (and if it's not already added and if the tool fits the role)
                randomRole = RNG.nextInt(roles.size());
                if(roles.get(randomRole).getMaxLoad() > volume) toolRoles.add(roles.get(randomRole).getName());
            }
            Tool tool = new Tool(name, volume, value, toolRoles.toArray(new String[toolRoles.size()]));
            allTools.add(tool);
            Log.log(Log.Level.NORMAL, "Adding tool: " + tool);
        }

        //add tools to roles
        allTools.forEach(tool -> tool.getRoles().forEach(role -> {
            roleMap.get(role).addTool(tool);
        }));

        return allTools;
    }

    /**
     * Generates a number of items dependent on config parameters
     * @return a list of items
     */
    public List<Item> generateItems(List<Tool> tools) {
        int baseItemAmount = RNG.nextInt(baseItemsMax - baseItemsMin + 1) + baseItemsMin;
        int resourcesAmount = RNG.nextInt(resourcesMax - resourcesMin + 1) + resourcesMin;

        List<Item> items = new Vector<>();

        // generate base items
        for(int i = 0; i < baseItemAmount + resourcesAmount; i++){
            Item item = new Item("item" + i, RNG.nextInt(maxVol - minVol + 1) + minVol,
                    RNG.nextInt(valueMax - valueMin + 1) + valueMin);
            items.add(item);
            baseItems.add(item);
        }

        resources.addAll(items.subList(baseItemAmount, items.size())); // determine resources
        itemGraph.add(baseItems);

        // generate assembled items
        int graphDepth = RNG.nextInt(graphDepthMax - graphDepthMin + 1) + graphDepthMin;
        int levelAmount = baseItemAmount; // only base items without resources, otherwise graph gets too big!
        for(int i = 1; i <= graphDepth; i++){
            levelAmount = Math.max(1, // at least 1 item per level
                    levelAmount - (RNG.nextInt(levelDecreaseMax - levelDecreaseMin + 1) + levelDecreaseMin));
            List<Item> levelItems = new ArrayList<>();
            for(int j = 1; j <= levelAmount; j++){
                // determine required items
                Map<Item, Integer> requiredItems = new HashMap<>();
                int requiredAmount = RNG.nextInt(maxReq - minReq + 1) + minReq;
                requiredAmount = Math.min(requiredAmount, baseItemAmount + resourcesAmount);
                // add item from one level beneath; if level beneath is level 0, take a resource
                List<Item> tmpItems = (i == 1)? new ArrayList<>(resources)
                                              : new ArrayList<>(itemGraph.get(i - 1));
                RNG.shuffle(tmpItems);
                requiredItems.put(tmpItems.get(0), RNG.nextInt(reqAmountMax - reqAmountMin + 1) + reqAmountMin);
                requiredAmount -= 1;
                // get list of possible levels and possible items
                List<Item> possibleItems = new ArrayList<>();
                if(i == 1){
                    possibleItems.addAll(itemGraph.get(0));
                } else {
                    //only use items up to level (i-2) to avoid high assembleValues
                    for (int k = 0; k < i - 1; k++) {
                        possibleItems.addAll(itemGraph.get(k));
                    }
                }
                possibleItems.remove(tmpItems.get(0)); // remove the item that was already added in the first step
                RNG.shuffle(possibleItems);
                // add amount of required items
                for (int l = 0; l < Math.min(requiredAmount, possibleItems.size()); l++) {
                    requiredItems.put(possibleItems.get(l), RNG.nextInt(reqAmountMax - reqAmountMin + 1) + reqAmountMin);
                }

                // calculate volume of assembled item
                int volume = 0;
                for(Map.Entry<Item,Integer> e: requiredItems.entrySet()){
                    volume += e.getKey().getVolume() * e.getValue();
                }
                // subtract random percentage (up to 50%)
                volume -= (int) (RNG.nextDouble() * .5 * volume);
                // ensure that at least the role with max capacity can carry this item
                if(volume > maxCapacity) volume = (int) (maxCapacity * .9);

                // create assembled item
                Item item = new Item("item" + items.size(), volume, 0);
                item.setRequiredItems(requiredItems);

                // determine required tools
                List<Tool> tempTools = new ArrayList<>(tools);
                if(RNG.nextDouble() < toolProbability){
                    RNG.shuffle(tempTools);
                    item.addRequiredTool(tempTools.get(0));
                    if(RNG.nextDouble() < toolProbability){
                        item.addRequiredTool(tempTools.get(1));
                    }
                }

                items.add(item);
                levelItems.add(item);
                assembledItems.add(item);
            }
            itemGraph.add(levelItems);
        }

        for(int i = 0; i < itemGraph.size(); i++){
            Log.log(Log.Level.NORMAL, "Generated item graph level " + i);
            for(Item item: itemGraph.get(i)){
                Log.log(Log.Level.NORMAL, "Generated item: " + item);
            }
        }
        return items;
    }

    /**
     * Generates a number of facilities dependent on config parameters
     * @return a list of facilities
     */
    public List<Facility> generateFacilities(WorldState world) {
        double minLat = world.getMinLat();
        double maxLat = world.getMaxLat();
        double minLon = world.getMinLon();
        double maxLon = world.getMaxLon();

        List<Facility> facilities = new ArrayList<>();
        List<Shop> shops = new ArrayList<>();
        List<ResourceNode> resourceNodes = new ArrayList<>();
        Set<Location> locations = new HashSet<>();

        // generate charging stations
        int chargingCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant

                int numberOfFacilities = 0;
                if(chargingDensity < 1){
                    if(RNG.nextDouble() < shopDensity) numberOfFacilities = 1;
                }
                else numberOfFacilities = new Float(chargingDensity).intValue();

                for(int i = 0; i < numberOfFacilities; i++){
                    ChargingStation charging = new ChargingStation("chargingStation" + chargingCounter,
                            getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize),
                            RNG.nextInt(rateMax - rateMin + 1) + rateMin);
                    facilities.add(charging);
                    locations.add(charging.getLocation());
                    chargingCounter++;
                }
            }
        }

        if(chargingCounter == 0){ // create at least 1 charging station
            ChargingStation charging = new ChargingStation("chargingStation" + chargingCounter,
                    getUniqueLocation(locations, world),
                    RNG.nextInt((rateMax-rateMin) + 1) + rateMin);
            facilities.add(charging);
            locations.add(charging.getLocation());
        }

        // generate empty shops
        int shopCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant

                int numberOfFacilities = 0;
                if(shopDensity < 1){
                    if(RNG.nextDouble() < shopDensity){
                        numberOfFacilities = 1;
                    }
                }
                else{
                    numberOfFacilities = new Float(shopDensity).intValue();
                }
                for(int i = 0; i < numberOfFacilities; i++){
                    Location loc = getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize);
                    Shop shop = new Shop("shop" + shopCounter, loc,RNG.nextInt(restockMax - restockMin + 1) + restockMin);
                    facilities.add(shop);
                    locations.add(shop.getLocation());
                    shops.add(shop);
                    shopCounter++;
                }
            }
        }
        if(shopCounter == 0){
            Shop shop = new Shop("shop" + shopCounter, getUniqueLocation(locations, world),
                    RNG.nextInt(restockMax - restockMin + 1) + restockMin);
            facilities.add(shop);
            locations.add(shop.getLocation());
            shops.add(shop);
        }
        // add base items, resources and tools to shops
        List<Item> shopItems = new ArrayList<>();
        shopItems.addAll(itemGraph.get(0));
        shopItems.addAll(allTools);
        List<Item> usedItems = new ArrayList<>();
        for(Shop shop: shops){
            int numberOfProducts = Math.min(RNG.nextInt(maxProd - minProd + 1) + minProd, shopItems.size());

            List<Item> unusedItems = new ArrayList<>(shopItems); // items not used for this shop
            for(int j = 0; j < numberOfProducts; j++){
                int productNumber = RNG.nextInt(unusedItems.size());
                Item item = unusedItems.get(productNumber);
                float priceAdd = (RNG.nextInt(priceAddMax - priceAddMin + 1) + priceAddMin) / 100.0f;
                int price = (int) (item.getValue() * priceAdd);
                shop.addItem(item, RNG.nextInt(amountMax - amountMin + 1) + amountMin, price);
                unusedItems.remove(productNumber);
                usedItems.add(item);
            }
        }
        shopItems.removeAll(usedItems);
        for(Item item: shopItems){
            int shopNumber = RNG.nextInt(shops.size());
            Shop shop = shops.get(shopNumber);
            float priceAdd = (RNG.nextInt((priceAddMax-priceAddMin) + 1) + priceAddMin) / 100.0f;
            int price = (int) (item.getValue() * priceAdd);
            shop.addItem(item, RNG.nextInt((amountMax-amountMin) + 1) + amountMin, price);
        }

        // generate dumps
        int dumpCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant
                int numberOfFacilities = 0;
                if(dumpDensity < 1){
                    if(RNG.nextDouble() < dumpDensity) numberOfFacilities = 1;
                }
                else numberOfFacilities = new Float(dumpDensity).intValue();
                for(int i = 0; i < numberOfFacilities; i++){
                    Location loc = getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize);
                    Dump dump1 = new Dump("dump" + dumpCounter, loc);
                    facilities.add(dump1);
                    locations.add(dump1.getLocation());
                    dumpCounter++;
                }
            }
        }
        if(dumpCounter == 0){
            Dump dump = new Dump("dump" + dumpCounter, getUniqueLocation(locations, world));
            facilities.add(dump);
            locations.add(dump.getLocation());
        }

        //generate workshops
        int workshopCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant
                int numberOfFacilities = 0;
                if(workshopDensity < 1){
                    if(RNG.nextDouble() < workshopDensity) numberOfFacilities = 1;
                }
                else numberOfFacilities = new Float(workshopDensity).intValue();
                for(int i = 0; i < numberOfFacilities; i++){
                    Location loc = getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize);
                    Workshop workshop = new Workshop("workshop" + workshopCounter, loc);
                    facilities.add(workshop);
                    locations.add(workshop.getLocation());
                    workshopCounter++;
                }
            }
        }
        if(workshopCounter == 0){
            Workshop workshop = new Workshop("workshop" + workshopCounter, getUniqueLocation(locations, world));
            facilities.add(workshop);
            locations.add(workshop.getLocation());
        }

        // generate storage
        int storageCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant
                int numberOfFacilities = 0;
                if(storageDensity < 1){
                    if(RNG.nextDouble() < storageDensity) numberOfFacilities = 1;
                }
                else numberOfFacilities = new Float(storageDensity).intValue();
                for(int i = 0; i < numberOfFacilities; i++){
                    Location loc = getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize);
                    Storage storage = new Storage("storage" + storageCounter, loc,
                            (RNG.nextInt(capacityMax - capacityMin + 1) + capacityMin),
                            world.getTeams().stream().map(TeamState::getName).collect(Collectors.toSet()));
                    facilities.add(storage);
                    locations.add(storage.getLocation());
                    storageCounter++;
                }
            }
        }
        if(storageCounter == 0){
            Storage storage = new Storage("storage" + storageCounter, getUniqueLocation(locations, world),
                    (RNG.nextInt(capacityMax - capacityMin + 1) + capacityMin),
                    world.getTeams().stream().map(TeamState::getName).collect(Collectors.toSet()));
            facilities.add(storage);
            locations.add(storage.getLocation());
        }

        // generate resource nodes
        int nodeCounter = 0;
        for (double a = minLat; a < maxLat; a += quadSize) {
            for (double b = minLon; b < maxLon; b += quadSize) { // (a,b) = corner of the current quadrant
                int numberOfFacilities = 0;
                if (resourceDensity < 1) {
                    if (RNG.nextDouble() < resourceDensity) numberOfFacilities = 1;
                }
                else numberOfFacilities = new Float(resourceDensity).intValue();
                for (int i = 0; i < numberOfFacilities; i++) {
                    Location loc = getUniqueLocationInBounds(locations, world, a, a + quadSize, b, b + quadSize);
                    ResourceNode node = new ResourceNode("resourceNode" + nodeCounter, loc,
                            resources.get(nodeCounter % resources.size()),
                            RNG.nextInt(gatherFrequencyMax - gatherFrequencyMin + 1) + gatherFrequencyMin);
                    facilities.add(node);
                    locations.add(node.getLocation());
                    resourceNodes.add(node);
                    nodeCounter++;
                }
            }
        }
        if(nodeCounter < resources.size()){
            for(int i = nodeCounter; i < resources.size(); i++){
                ResourceNode node = new ResourceNode("resourceNode" + nodeCounter, getUniqueLocation(locations, world),
                        resources.get(nodeCounter % resources.size()),
                        RNG.nextInt(gatherFrequencyMax - gatherFrequencyMin + 1) + gatherFrequencyMin);
                facilities.add(node);
                locations.add(node.getLocation());
                resourceNodes.add(node);
                nodeCounter++;
            }
        }

        for(ResourceNode node: resourceNodes){
            Log.log(Log.Level.NORMAL, "Added resource node: " + node.getName() + ": " + node.getResource().getName() +
                    " " + node.getLocation().getLat() + ", " + node.getLocation().getLon());
        }

        for(Facility fac: facilities) Log.log(Log.Level.NORMAL, "Added facility: " + fac);

        return facilities;
    }

    /**
     * Tries to get a new random location with < 1000 attempts.
     * @param world the world to look for a location in
     * @return a new random location or the "center" of the map if no such location could be found in reasonable time
     */
    private Location getRandomLocation(WorldState world){
        return world.getMap().getRandomLocation(new HashSet<>(Collections.singletonList(GraphHopperManager.PERMISSION_ROAD)), 1000);
    }

    /**
     * Tries to get a new random location within certain bounds
     */
    private Location getRandomLocationInBounds(WorldState world, double minLat, double maxLat, double minLon, double maxLon){
        return world.getMap().getRandomLocationInBounds(new HashSet<>(Collections.singletonList(GraphHopperManager.PERMISSION_ROAD)),
                1000, minLat, maxLat, minLon, maxLon);
    }

    /**
     * Tries to get a new random location that is not already in use
     * @param locations locations that are already in use
     * @param world the world to look for a location in
     */
    private Location getUniqueLocation(Set<Location> locations, WorldState world){
        Location loc = getRandomLocation(world);
        for(int i=0; i<100; i++){
            if(locations.contains(loc)){
                loc = getRandomLocation(world);
                continue;
            }
            return loc;
        }
        return loc;
    }

    /**
     * Tries to get a unique location within certain bounds
     */
    private Location getUniqueLocationInBounds(Set<Location> locations, WorldState world, double minLat, double maxLat, double minLon, double maxLon){
        Location loc = getRandomLocationInBounds(world, minLat, maxLat, minLon, maxLon);
        for(int i=0; i<100; i++){
            if(locations.contains(loc)){
                loc = getRandomLocationInBounds(world, minLat, maxLat, minLon, maxLon);
                continue;
            }
            return loc;
        }
        return loc;
    }

    /**
     * Randomly picks a number of items to use for a job
     * @param possibleItems list of all items that can be used for the job
     */
    private Map<Item, Integer> determineJobItems(List<Item> possibleItems, boolean mission){

        Map<Item, Integer> result = new HashMap<>();
        int currentDifficulty = 0;
        int difficulty = RNG.nextInt((mission? missionDifficultyMax : difficultyMax) - difficultyMin + 1) + difficultyMin;

        int numberOfItems = Math.min(RNG.nextInt(productTypesMax - productTypesMin + 1) + productTypesMin,
                possibleItems.size());

        // choose items for job
        RNG.shuffle(possibleItems);
        int currentNumberOfProducts = 0;
        for (Item tmpJobItem : possibleItems) {
            if (currentDifficulty + tmpJobItem.getAssembleValue() < difficulty) {
                currentDifficulty += tmpJobItem.getAssembleValue();
                result.put(tmpJobItem, 1);
                currentNumberOfProducts++;
            }
            if (currentNumberOfProducts >= numberOfItems) break;
        }

        // determine quantities for each item
        List<Item> itemList = new ArrayList<>(result.keySet());
        while(currentDifficulty < difficulty && (!itemList.isEmpty())){
            int itemIndex = RNG.nextInt(itemList.size());
            if(currentDifficulty + itemList.get(itemIndex).getAssembleValue() < difficulty){
                currentDifficulty += itemList.get(itemIndex).getAssembleValue();
                int amount = result.get(itemList.get(itemIndex));
                result.put(itemList.get(itemIndex), amount + 1);
            }
            else itemList.remove(itemIndex);
        }

        // safeguard
        if(result.isEmpty()) result.put(possibleItems.get(0), 1);

        return result;
    }

    /**
     * Generates a number of jobs dependent on config parameters
     * @return a set of jobs
     * @param stepNo the number of the current step
     */
    public Set<Job> generateJobs(int stepNo, WorldState world) {
        Set<Job> jobs = new HashSet<>();
        List<Item> tmpJobItems = new ArrayList<>();

        if (difficultyMin == 0 && difficultyMax == 0 && missionDifficultyMax == 0) {
            tmpJobItems.addAll(baseItems); // only require base items in this case
        } else {
            tmpJobItems.addAll(assembledItems);
        }

        double jobProb = Math.exp(-1d * (double)stepNo/(double)world.getSteps()) * rate;
        if(RNG.nextDouble() <= jobProb){ // create a new job

            boolean createMission = stepNo >= missionEnd && RNG.nextDouble() <= missionProbability;

            List<Storage> storageList = new ArrayList<>(world.getStorages());
            Storage storage = storageList.get(RNG.nextInt(storageList.size()));
            Map<Item, Integer> jobItems = determineJobItems(tmpJobItems, createMission);
            int length = RNG.nextInt(timeMax - timeMin + 1) + timeMin;

            int reward = computeReward(jobItems);
            int rewardAdd = (int) (reward*(RNG.nextInt((rewardAddMax - rewardAddMin) + 1) + rewardAddMin)/100.0f);
            reward += rewardAdd;
            if (difficultyMin == 0 && difficultyMax == 0 && missionDifficultyMax == 0) reward = jobItems.keySet().size() * 100;

            if(!createMission){

                if (difficultyMin == 0 && difficultyMax == 0 && missionDifficultyMax == 0) {
                    reward = jobItems.keySet().size() * 100;
                }

                Job job;
                if(RNG.nextDouble() <= auctionProbability){
                    // create auction
                    int auctionTime = RNG.nextInt(auctionTimeMax - auctionTimeMin + 1) + auctionTimeMin;
                    int fine;
                    int fineMod = 1 + RNG.nextInt(fineAdd + fineSub);
                    if(fineMod > fineSub) {
                        fine = reward + (int) (reward * ((fineMod - fineSub) / 100.0f));
                    } else {
                        fine = reward - (int) (reward * (fineMod / 100.0f));
                    }
                    maxRewardAdd = 1 + RNG.nextInt(rewardAddMax);
                    reward += (int) (reward * maxRewardAdd / 100.0f);
                    job = new AuctionJob(reward, storage, stepNo + 1, stepNo + 1 + length, auctionTime, fine);
                }
                else {
                    // create regular job
                    job = new Job(reward, storage, stepNo + 1, stepNo + 1 + length, JobData.POSTER_SYSTEM);
                }
                for(Item item: jobItems.keySet()) job.addRequiredItem(item, jobItems.get(item));
                jobs.add(job);
            } else {
                // create mission
                missionEnd = stepNo + 1 + length;
                int fine;
                int fineMod = 1 + RNG.nextInt(fineAdd + fineSub);
                if(fineMod > fineSub){
                    fine = reward + (int) (reward * ((fineMod - fineSub) / 100.0f));
                } else {
                    fine = reward - (int) (reward * (fineMod / 100.0f));
                }

                for(TeamState team: world.getTeams()){ // one mission instance for each team
                    Mission mission = new Mission(reward, storage, stepNo + 1, stepNo + 1 + length, fine, team, String.valueOf(missionID));
                    for(Item item: jobItems.keySet()) mission.addRequiredItem(item, jobItems.get(item));
                    jobs.add(mission);
                }
                missionID++;
            }
        }

        // Log jobs
        for(Job job: jobs){
            List<String> reqItems = new ArrayList<>();
            for(Item item: job.getRequiredItems().getStoredTypes()){
                reqItems.add(job.getRequiredItems().getItemCount(item) + "x " + item.getName());
            }
            Log.log(Log.Level.NORMAL, "New job: " + job.getName() + ": " + String.join(", ", reqItems) + " " + job.getReward() +
                    " " + job.getBeginStep() + " " + job.getEndStep() + " " + job.getStorage() + " " + job.getClass().getSimpleName());
        }

        return jobs;
    }

    /**
     *
     * @param requiredItems items required to complete the job
     * @return reward for a job with the corresponding required items
     */
    private int computeReward(Map<Item, Integer> requiredItems){
        int reward = 0;
        for(Item reqItem: requiredItems.keySet()){
            for(int i = 0; i < requiredItems.get(reqItem); i++){
                for(Item reqBaseItem: reqItem.getRequiredBaseItems().keySet()){
                    reward += reqItem.getRequiredBaseItems().get(reqBaseItem) * reqBaseItem.getValue();
                }
                reward += reqItem.getAssembleValue() * 100;
            }
        }
        return reward;
    }

    /**
     * Progresses each facility's blackout and may generate new ones.
     * @param world the current world state
     */
    public void handleBlackouts(WorldState world){

        // manage facilities affected by blackout
        List<Facility> workingFacilities = new ArrayList<>();
        for(Facility facility: blackoutFacilities){
            facility.stepBlackoutCounter();
            if(facility.stepBlackoutCounter() == 0) workingFacilities.add(facility);
        }
        blackoutFacilities.removeAll(workingFacilities);

        // initiate new blackout
        if(RNG.nextDouble() < blackoutProbability){
            List<Facility> facilities = new ArrayList<>(world.getChargingStations());
            Facility targetFacility = facilities.get(RNG.nextInt(facilities.size()));
            if(!blackoutFacilities.contains(targetFacility)){
                int duration = RNG.nextInt(blackoutTimeMax - blackoutTimeMin + 1) + blackoutTimeMin;
                targetFacility.initiateBlackout(duration);
                blackoutFacilities.add(targetFacility);
                Log.log(Log.Level.NORMAL, "New blackout in " + targetFacility.getName() + ", duration " + duration + " steps.");
            }
        }
    }

    /**
     * Adds a facility to the list of facilities affected by blackout (for testing)
     */
    public void addToBlackoutFacilities(Facility facility){ blackoutFacilities.add(facility);}

    /**
     * @return a list containing all resources
     */
    public List<Item> getResources(){ return resources;}

    /**
     * @return a list containing all base items
     */
    public List<Item> getBaseItems(){ return baseItems;}
}
