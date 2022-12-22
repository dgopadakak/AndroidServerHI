package Firm;

public class Pharmacy
{
    String nameOfPharmacy;
    String address;
    int num;
    String timeOpen;
    String timeClose;
    int parkingSpaces;
    int isDelivery;
    String comment;

    public Pharmacy(String nameOfPharmacy, String address, int num, String timeOpen, String timeClose, int parkingSpaces, int isDelivery, String comment)
    {
        this.nameOfPharmacy = nameOfPharmacy;
        this.address = address;
        this.num = num;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.parkingSpaces = parkingSpaces;
        this.isDelivery = isDelivery;
        this.comment = comment;
    }
}
