import org.junit.Before;
import org.junit.Test;
import service.OptionService;
import static junit.framework.TestCase.assertNotNull;

public class OptionServiceTest {

    private OptionService optionService;

    @Before
    public void initialize() {
        optionService = new OptionService(GlobalProperties.getInstance().getApiKey());
    }

    @Test
    public void testAPIConnection() {
        assertNotNull(optionService.getOptionChain("SPY"));
    }
}
