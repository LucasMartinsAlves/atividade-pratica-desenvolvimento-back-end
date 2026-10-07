package br.com.lucas.baozi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:teste", "spring.jpa.hibernate.ddl-auto=create-drop"})
@AutoConfigureMockMvc
@Transactional
class BaoziStoreApplicationTests {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    private long criar(String rota, String dados) throws Exception {
        var resposta = mvc.perform(post(rota).contentType(MediaType.APPLICATION_JSON).content(dados))
                .andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();
        return json.readTree(resposta.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void cicloCompletoComVinculosEExclusao() throws Exception {
        long cliente = criar("/clientes", "{\"nome\":\"Lucas Martins Alves3926553\",\"clienteDesde\":\"2026-10-07\"}");
        long produto = criar("/produtos", "{\"nome\":\"Baozi de carne\",\"preco\":8.50,\"estoque\":true}");
        long pedido = criar("/pedidos", "{\"clienteId\":" + cliente + ",\"produtoId\":" + produto + ",\"quantidade\":3}");
        for (var rota : new String[]{"/clientes", "/produtos", "/pedidos"}) {
            mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        }
        mvc.perform(get("/clientes/" + cliente)).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Lucas Martins Alves3926553"));
        mvc.perform(get("/produtos/" + produto)).andExpect(status().isOk()).andExpect(jsonPath("$.preco").value(8.5));
        mvc.perform(get("/pedidos/" + pedido)).andExpect(status().isOk()).andExpect(jsonPath("$.cliente.id").value(cliente));
        mvc.perform(delete("/pedidos/" + pedido)).andExpect(status().isNoContent());
        mvc.perform(delete("/clientes/" + cliente)).andExpect(status().isNoContent());
        mvc.perform(delete("/produtos/" + produto)).andExpect(status().isNoContent());
        mvc.perform(get("/pedidos/" + pedido)).andExpect(status().isNotFound());
        mvc.perform(delete("/clientes/" + cliente)).andExpect(status().isNotFound());
    }

    @Test
    void rejeitaDadosInvalidosEReferenciasInexistentes() throws Exception {
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"\",\"clienteDesde\":\"2026-10-07\"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Baozi\",\"preco\":-1,\"estoque\":true}")).andExpect(status().isBadRequest());
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\":1,\"produtoId\":1,\"quantidade\":0}")).andExpect(status().isBadRequest());
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\":999,\"produtoId\":999,\"quantidade\":1}")).andExpect(status().isNotFound());
        mvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON)
                .content("{")).andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaProdutoIndisponivel() throws Exception {
        long cliente = criar("/clientes", "{\"nome\":\"Lucas\",\"clienteDesde\":\"2026-10-07\"}");
        long produto = criar("/produtos", "{\"nome\":\"Baozi\",\"preco\":8.50,\"estoque\":false}");
        mvc.perform(post("/pedidos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clienteId\":" + cliente + ",\"produtoId\":" + produto + ",\"quantidade\":1}"))
                .andExpect(status().isConflict());
    }
}
