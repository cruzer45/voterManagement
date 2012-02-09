package bz.voter.management.utils

enum AddressEnum {
    REGISTRATION('Registration'), 
    ALTERNATE('Alternate'), 
    WORK('Work')

    final String name

    AddressEnum(String value){
        this.name = value
    }

    public String value(){
        name
    }
}
