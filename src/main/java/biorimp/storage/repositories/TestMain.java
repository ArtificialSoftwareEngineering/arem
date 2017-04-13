package biorimp.storage.repositories;

import biorimp.storage.entities.Register;

/**
 * Created by developer on 12/30/15.
 */
public class TestMain {


    public static void main(String args[]) {
        Register r = new Register("1", "123254", 233, "", "", "", "", "");
        RegisterRepository repo = new RegisterRepository();
        Register r2 = repo.getRegister("1", "123254", "", "", "", "");
        System.out.println(r2.getValue());
        repo.insertRegister(r);
        for (Register reg : repo.fetchAll()) {
            System.out.println(reg.getClasss());
        }
        System.out.println("value:" + repo.getRegistersByClass("1", "", "", "", "", "").get(0).getValue());
    }
}
