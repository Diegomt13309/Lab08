package Model;

import java.util.ArrayList;

public class SingletonFormList {

    private static SingletonFormList mInstance;
    private static ArrayList<User> list = null;

    public static SingletonFormList getInstance() {
        if(mInstance == null)
            mInstance = new SingletonFormList();

        return mInstance;
    }

    private SingletonFormList() {
        list = new ArrayList<>();
    }

    public ArrayList<User> getArray() {
        return this.list;
    }

    public void addToArray(User value) {

        list.add(value);
    }
    public void removeFromArray(User value){
        list.remove(value);
    }
}
