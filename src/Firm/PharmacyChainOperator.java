package Firm;

import java.util.ArrayList;
import java.util.Objects;

public class PharmacyChainOperator
{
    private int id;
    private ArrayList<PharmacyChain> pharmacyChains = new ArrayList<>();

    public void addPharmacy(String groupName, Pharmacy pharmacy)
    {
        boolean isNewGroupNeeded = true;
        for (PharmacyChain pharmacyChain : pharmacyChains)
        {
            if (Objects.equals(pharmacyChain.name, groupName))
            {
                isNewGroupNeeded = false;
                pharmacyChain.listOfPharmacies.add(pharmacy);
                break;
            }
        }
        if (isNewGroupNeeded)
        {
            ArrayList<Pharmacy> tempArrayList = new ArrayList<>();
            tempArrayList.add(pharmacy);
            pharmacyChains.add(new PharmacyChain(groupName, tempArrayList));
        }
    }

    public void delPharmacy(int groupId, int examId)
    {
        pharmacyChains.get(groupId).listOfPharmacies.remove(examId);
    }

    public void editPharmacy(int groupId, int examId, Pharmacy newPharmacy)
    {
        pharmacyChains.get(groupId).listOfPharmacies.set(examId, newPharmacy);
    }

    public ArrayList<PharmacyChain> getPharmacyChains()
    {
        return pharmacyChains;
    }

    public void setPharmacyChains(ArrayList<PharmacyChain> pharmacyChains)
    {
        this.pharmacyChains = pharmacyChains;
    }
}
