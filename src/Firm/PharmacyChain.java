package Firm;

import java.util.ArrayList;

public class PharmacyChain
{
    String name;
    ArrayList<Pharmacy> listOfPharmacies;

    public PharmacyChain(String name, ArrayList<Pharmacy> listOfPharmacies)
    {
        this.name = name;
        this.listOfPharmacies = listOfPharmacies;
    }
}
