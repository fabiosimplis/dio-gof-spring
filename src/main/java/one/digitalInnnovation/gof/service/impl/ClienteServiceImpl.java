package one.digitalInnnovation.gof.service.impl;

import one.digitalInnnovation.gof.model.Cliente;
import one.digitalInnnovation.gof.model.ClienteRepository;
import one.digitalInnnovation.gof.model.Endereco;
import one.digitalInnnovation.gof.model.EnderecoRepository;
import one.digitalInnnovation.gof.service.ClienteService;
import one.digitalInnnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService cepService;
    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente =
                clienteRepository.findById(id);
        return cliente.orElse(null);
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }



    @Override
    public void atualizar(Long id, Cliente cliente) {

        Optional<Cliente> clienteBuscado = clienteRepository.findById(id);
        if (clienteBuscado.isPresent()){
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = cepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return null;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
