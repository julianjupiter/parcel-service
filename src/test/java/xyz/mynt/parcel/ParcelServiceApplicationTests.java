package xyz.mynt.parcel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import xyz.mynt.parcel.exception.BeanValidationException;
import xyz.mynt.parcel.exception.VoucherException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParcelServiceApplicationTests {

    @Test
    void reject(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 55,
                	"height": 10,
                	"width": 10,
                	"length": 10
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("REJECT"))
                .andExpect(jsonPath("$.data.cost").value(0.00));
    }

    @Test
    void heavyParcel(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 15,
                	"height": 10,
                	"width": 10,
                	"length": 10
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("HEAVY_PARCEL"))
                .andExpect(jsonPath("$.data.cost").value(300.00));
    }

    @Test
    void smallParcel(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 10
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("SMALL_PARCEL"))
                .andExpect(jsonPath("$.data.cost").value(30.00));
    }

    @Test
    void mediumParcel(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 15
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("MEDIUM_PARCEL"))
                .andExpect(jsonPath("$.data.cost").value(60.00));
    }

    @Test
    void largeParcel(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 30
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("LARGE_PARCEL"))
                .andExpect(jsonPath("$.data.cost").value(150.00));
    }

    @Test
    void largeParcelWithDiscount(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 30,
                	"voucherCode": "MYNT"
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("LARGE_PARCEL"))
                .andExpect(jsonPath("$.data.cost").value(150.00))
                .andExpect(jsonPath("$.data.voucherCode").value("MYNT"));
    }

    @Test
    void largeParcelWithExpiredVoucherCode(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 30,
                	"voucherCode": "skdlds"
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Expired voucher code"));
    }

    @Test
    void largeParcelWithInvalidVoucherCode(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10,
                	"width": 10,
                	"length": 30,
                	"voucherCode": "xyz"
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Invalid voucher code"))
                .andExpect(result -> assertInstanceOf(VoucherException.class, result.getResolvedException()));
    }

    @Test
    void invalidRequestContent(@Autowired MockMvc mvc) throws Exception {
        var content = """
                {
                	"weight": 8,
                	"height": 10
                }
                """;
        mvc.perform(post("/v1/parcels/delivery-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Invalid request content."))
                .andExpect(result -> assertInstanceOf(BeanValidationException.class, result.getResolvedException()));
    }

}
