package assignment1;

public abstract class EmissionSource {

    private String SourceID;

    private String category;

    private String date;

    private String userName;

    public EmissionSource(String sourceID, String category, String date, String userName) {
        this.SourceID = sourceID;
        this.category = category;
        this.date = date;
        this.userName = userName;
    }

    

    public String getSourceID(){

        return this.SourceID;
    }

    public void setSourceID(String id){

        this.SourceID=id;
    }

    public String getCategory(){

        return this.category;
    }
    public void setCategory(String cat){

        this.category=cat;
    }

    public String getDate(){

        return this.date;
    }
    public void setDate(String date){

        this.date=date;
    }

    public String getUserName(){

        return this.userName;
    }
    public void setUserName(String name){

        this.userName=name;
    }

    public abstract double calculateEmission();

    public String toString(){

        return this.userName + ": " + this.SourceID+ "| " + this.category + "| " + this.date;
    }
    
}
