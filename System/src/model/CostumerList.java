package model;

import java.util.ArrayList;

public class CostumerList {
    private ArrayList<Costumer> costumerList=new ArrayList<>();

    public void addCostumer(Costumer costumer) {
        this.costumerList.add(costumer);
    }

    public void removeCostumer(Costumer costumer) {
        this.costumerList.remove(costumer);
    }

    public void removeCostumerById(int costumer) {
        for (Costumer c : this.costumerList) {
            if (c.getCostumerId() == costumer) {
                this.costumerList.remove(c);
            }
        }
    }

    public Costumer getCostumerById(int costumer) {
        for (Costumer c : this.costumerList) {
            if (c.getCostumerId() == costumer) {
                return c;
            }
        }
        return null;
    }

    public Costumer[] getAllCostumers() {
        return this.costumerList.toArray(new Costumer[this.costumerList.size()]);
    }
    //sortBY skal laves

    public CostumerList getCostumerByPhone(long phone) {
       CostumerList list = new CostumerList();
        for (Costumer c : this.costumerList) {
            if (c.getPhone() == phone) {
                list.addCostumer(c);
            }
        }
        return list;
    }
    public CostumerList getCostumerByName(String name) {
        CostumerList list = new CostumerList();
        for (Costumer c : this.costumerList) {
            if (c.getName() == name) {
                list.addCostumer(c);
            }
        }
        return list;
    }
    public CostumerList getCostumerByEmail(String email) {
        CostumerList list = new CostumerList();
        for (Costumer c : this.costumerList) {
            if (c.getEmail() == email) {
                list.addCostumer(c);
            }
        }
        return list;
    }
}
