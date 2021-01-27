


public class TWELVE {
    
    public static void main(String[] args) throws Exception{
        args[0] = "../pride-and-prejudice.txt";
        WordFrequencyController wfcontroller = new WordFrequencyController();
        wfcontroller.dispatch(new String[] {"init",args[0]});
        wfcontroller.dispatch(new String[] {"run"});
    }


}
