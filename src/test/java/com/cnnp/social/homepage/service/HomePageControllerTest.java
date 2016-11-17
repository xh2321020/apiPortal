package com.cnnp.social.homepage.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class HomePageControllerTest {
    private MockMvc mockMvc;
    private RestTemplate restTemplate = new TestRestTemplate();

    @Before
    public void setUp() throws Exception {

    }

    /**
     * Method: save(@RequestBody HomePageInfoDto hp)
     */
    @Test
    public void testSaveHp() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: edit(@RequestBody HomePageInfoDto hp)
     */
    @Test
    public void testEdit() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: edittype(@RequestParam long hpid, @RequestParam String type)
     */
    @Test
    public void testEdittype() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: view(@PathVariable("userid") String userid)
     */
    @Test
    public void testView() throws Exception {
        RequestBuilder request = get("http://localhost:8000/api/V1.0/homepage/homepagestyle/edittype?type=del&styleid=0");
        mockMvc.perform(request)
            .andExpect(status().isOk());
    }

    /**
     * Method: viewColumn(@RequestParam long hpid)
     */
    @Test
    public void testViewColumn() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: save(@RequestBody HomePageArticlecatDto column)
     */
    @Test
    public void testSaveColumn() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: columnedit(@RequestBody HomePageArticlecatDto column)
     */
    @Test
    public void testColumnedit() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editcolumntype(@RequestParam long columnid, @RequestParam String type)
     */
    @Test
    public void testEditcolumntype() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: viewForm(@RequestParam long hpid)
     */
    @Test
    public void testViewForm() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editForm(@RequestBody HomePageFormDto form)
     */
    @Test
    public void testEditForm() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: save(@RequestBody HomePageFormDto form)
     */
    @Test
    public void testSaveForm() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: del(@RequestParam long formid)
     */
    @Test
    public void testDel() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: viewStyle(@PathVariable("hpid") long hpid)
     */
    @Test
    public void testViewStyle() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: save(@RequestBody HomePageStyleDto style)
     */
    @Test
    public void testSaveStyle() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: styleedit(@RequestBody HomePageStyleDto style)
     */
    @Test
    public void testStyleedit() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editstyletype(@RequestParam long styleid, @RequestParam String type)
     */
    @Test
    public void testEditstyletype() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: enableStyle(@PathVariable("styleid") long styleid)
     */
    @Test
    public void testEnableStyle() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: viewHpall(@PathVariable("hpid") long hpid)
     */
    @Test
    public void testViewHpall() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: prviewHpall(@PathVariable("styleid") long styleid)
     */
    @Test
    public void testPrviewHpall() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: viewFormall()
     */
    @Test
    public void testViewFormall() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: findHomePageCompanySector()
     */
    @Test
    public void testFindHomePageCompanySector() throws Exception {
//TODO: Test goes here... 
    }


} 
