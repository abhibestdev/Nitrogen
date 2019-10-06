package me.abhi.nitrogen.managers;

import me.abhi.nitrogen.kit.Kit;
import me.abhi.nitrogen.kits.DistanceArcherKit;
import me.abhi.nitrogen.kits.FishermanKit;
import me.abhi.nitrogen.kits.IronKit;
import me.abhi.nitrogen.manager.Manager;
import me.abhi.nitrogen.manager.ManagerHandler;

import java.util.ArrayList;
import java.util.List;

public class KitsManager extends Manager {

    private List<Kit> kitList;

    public KitsManager(ManagerHandler managerHandler) {
        super(managerHandler);
        loadKits();
    }

    private void loadKits() {
        this.kitList = new ArrayList<>();
        addKit(new DistanceArcherKit());
        addKit(new FishermanKit());
        addKit(new IronKit());
    }

    private void addKit(Kit kit) {
        this.kitList.add(kit);
    }

    public List<Kit> getKitList() {
        return kitList;
    }
}
