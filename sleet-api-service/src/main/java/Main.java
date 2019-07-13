import service.OptionService;

public class Main {

    public static void main (String[] args) {

        System.out.println("Initializing Sleet.......");

        OptionService optionService = new OptionService();
        String optionChainJson = optionService.getOptionChain("SPY", "5");

        System.out.println(optionChainJson);
    }
}
