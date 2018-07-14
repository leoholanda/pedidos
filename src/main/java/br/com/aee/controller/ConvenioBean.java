package br.com.aee.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

import br.com.aee.model.Convenio;
import br.com.aee.model.Status;
import br.com.aee.repository.ConvenioRepository;
import br.com.aee.util.JsfUtil;

@Named("convenioMB")
@ViewScoped
public class ConvenioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ConvenioRepository repository;
    
    private Convenio convenio;
    
    private List<Convenio> listaConvenios;
    
    private List<Convenio> listaServicos;
    
    private List<Convenio> listaServicosIndex;
    
    private List<Convenio> listaConveniosIndex;
    
    private List<Convenio> listaPlanoDeSaude;

    private List<Convenio> listaTudo;

    private String searchValue;

	private String fileName;
	
	@PostConstruct
	public void init() {
		convenio = new Convenio();
		listaPlanoDeSaude = repository.findBySomentePlanoDeSaude();
		listaConvenios = repository.findAllConvenios();
		listaServicos = repository.findAllServicos();
		
		listaConveniosIndex = repository.findAllConveniosIndex();
		listaServicosIndex = repository.findAllServicosIndex();
		
        listaTudo = repository.findAll();
	}

	
	// Actions
    
    /**
     * Salva convenio
     */
    public void save() {
        try {
            if (validacaoNomeConvenio()) {
            	semImagem();
                repository.save(convenio);
                
                convenio = new Convenio();

                JsfUtil.info("Salvo com sucesso!");

            } else {
                JsfUtil.error("Convênio existente");
            }

        } catch (Exception e) {
            JsfUtil.fatal("Algo deu errado, tente novamente!");
            e.getMessage();
        }
    }
    
    public void semImagem() {
    	if(convenio.getNomeArquivo() == null) {
    		convenio.setNomeArquivo("sem-imagem.png");
    	} else {
    		convenio.setNomeArquivo(fileName);
    	}
    }

    /**
     * Atualiza convenio
     */
    public void update() {
        try {
            repository.save(convenio);

            JsfUtil.info("Atualizado com sucesso!");

        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    /**
     * Desativa convenio
     */
    public void desativarConvenio() {
        try {
        	convenio.setStatus(Status.DESATIVADO);
            repository.save(convenio);
            convenio = new Convenio();

            JsfUtil.info("Desativado com sucesso!");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Pesquisa convenio por nome
     */
    public void pesquisar() {
        listaConvenios = repository.findByNomeLike("%" + searchValue + "%");
        System.out.println(searchValue);

        if (listaConvenios.isEmpty()) {
            JsfUtil.error("Nenhum registro encontrado");
        }

    }

    /**
     * Autocomplete convenio nome
     *
     * @param nome
     * @return
     */
    public List<Convenio> autocompleteConvenio(String nome) {
        nome = nome.toUpperCase();
        return repository.findByNomeLike(nome + "%");
    }

    /**
     * Seleciona setor na tabela
     *
     * @param event
     * @return
     */
    public Convenio selecionaConvenioNaTabela(SelectEvent event) {
        return convenio;
    }

    /**
     * Busca id convenio
     */
    public void buscar() {
        convenio = repository.findBy(convenio.getId());
    }
    
    public boolean verificaNomeExistente(String nome) {
        return repository.findAllByName(nome).isEmpty();
    }
    
    /**
     * Upload da imagem
     * @param event
     */
    public void enviarArquivo(FileUploadEvent event) {
		fileName = getRandomImageName() + ".png";

		convenio.setNomeArquivo(fileName);

		try {
			copyFile(event.getFile().getInputstream(), fileName);
			System.out.println(">>> Imagem enviada com sucesso " + fileName);
			JsfUtil.warning("Imagem enviada com sucesso");
		} catch (IOException e) {
			e.printStackTrace();
			JsfUtil.error("Falha ao enviar imagem");
		}
	}
    
    /**
     * ID para foto
     * @return
     */
    private String getRandomImageName() {
		int i = (int) (Math.random() * 10000000);

		return String.valueOf(i);
	}
    
    /**
     * Envia imagem para destinatario
     * @param stream
     * @param fileName
     */
    private void copyFile(InputStream stream, String fileName) {
		try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String destination = servletContext.getRealPath("") + "resources" + File.separator + "images" + File.separator;
			
			System.out.println(destination);
			OutputStream out = new FileOutputStream(new File(destination + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = stream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			stream.close();
			out.flush();
			out.close();

			System.out.println(">>> Nova imagem enviada");
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
    
    
    // Listing
    
    /**
     * Lista setor por ordem * alfabetica
     *
     * @return
     */
    public List<Convenio> getListaConvenios() {
        return listaConvenios;
    }
    
    public List<Convenio> getListaPlanoDeSaude() {
    	return listaPlanoDeSaude;
    }
    
    public List<Convenio> getListaServicos() {
		return listaServicos;
	}

    /**
     * Verifica se a lista é vazia
     *
     * @return
     */
    public Boolean getListEmpty() {
        return !repository.findByNomeLike("%" + searchValue + "%").isEmpty();

    }

    
    // Validations
    
    /**
     * Valida existencia de registro nome convenio
     *
     * @return
     */
    public Boolean validacaoNomeConvenio() {
        return repository.findAllByName(convenio.getNome()).isEmpty();
    }

    public boolean isConvenioExiste() {
        return this.getConvenio().getId() != null;
    }
    
    /**
     * Checa nome no ato do cadastro
     */
    public void verificaNomeExistente() {
        if (!validacaoNomeConvenio()) {
            JsfUtil.error("Nome do convênio ja existe");
        }
    }


    public Convenio getConvenio() {
        if (convenio == null) {
            convenio = new Convenio();
        }
        return convenio;
    }

    // Getters and Setters
    public String getSearchValue() {
        return searchValue;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public void setListaConvenios(List<Convenio> listaConvenios) {
        this.listaConvenios = listaConvenios;
    }
    
    public void setListaServicos(List<Convenio> listaServicos) {
		this.listaServicos = listaServicos;
	}

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue.toUpperCase();
    }

    public List<Convenio> getListaTudo() {
        return listaTudo;
    }

    public void setListaTudo(List<Convenio> listaTudo) {
        this.listaTudo = listaTudo;
    }
    
    public List<Convenio> getListaConveniosIndex() {
		return listaConveniosIndex;
	}
    
    public List<Convenio> getListaServicosIndex() {
		return listaServicosIndex;
	}
}
