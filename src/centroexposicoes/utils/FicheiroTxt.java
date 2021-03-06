/**
 * Fornece as classes utilitárias para o centro de exposições.
 */
package centroexposicoes.utils;

import centroexposicoes.model.Candidatura;
import centroexposicoes.model.CentroExposicoes;
import centroexposicoes.model.Demonstracao;
import centroexposicoes.model.Exposicao;
import centroexposicoes.model.Fae;
import centroexposicoes.model.ListaCandidaturas;
import centroexposicoes.model.ListaFaes;
import centroexposicoes.model.ListaOrganizadores;
import centroexposicoes.model.Local;
import centroexposicoes.model.Organizador;
import centroexposicoes.model.Produto;
import centroexposicoes.model.RegistoExposicoes;
import centroexposicoes.model.RegistoRepresentantes;
import centroexposicoes.model.Representante;
import centroexposicoes.model.Utilizador;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Lê os dados do centro de exposições num ficheiro de texto.
 *
 * @author Daniel Gonçalves 1151452
 * @author Ivo Ferro 1151159
 */
public class FicheiroTxt {

    /**
     * Pasta onde os ficheiros de texto estão guardados.
     */
    private static final String PASTA_FICHEIROS = "ficheiroTxt/";
    /**
     * Ficheiro onde os utilizadores funcionários estão guardados.
     */
    private static final String FICHEIRO_USERS_FUNCIONARIOS = "registoUtilizadoresFuncionarios.txt";
    /**
     * Ficheiro onde os representantes estão guardados.
     */
    private static final String FICHEIRO_REPS = "registoRepresentantes.txt";
    /**
     * Pasta das exposições.
     */
    private static final String PASTA_EXPOS = "exposicoes/";

    /**
     * Scanner para o input.
     */
    private static Scanner input;

    /**
     * Carega o centro de exposições a partir de ficheiros de texto.
     *
     * @return centro de exposições
     */
    public static CentroExposicoes carregarCentroExposicoes() {

        List<Representante> registoReps = carregarReps();
        List<Utilizador> listaUsersFuncionarios = carregarUsersFuncionarios();
        List<Exposicao> listaExposicoes = new ArrayList<>();

        File folderExposicoes = new File(PASTA_FICHEIROS + PASTA_EXPOS);
        for (File ficheiroExposicao : folderExposicoes.listFiles()) {

            String caminho = ficheiroExposicao.toString();

            if (ficheiroExposicao.isFile() && caminho.contains(".txt")) {
                listaExposicoes.add(carregarExposicao(listaUsersFuncionarios, ficheiroExposicao));
            }
        }

        CentroExposicoes ce = new CentroExposicoes(new RegistoExposicoes(listaExposicoes), new RegistoRepresentantes(registoReps));

        return ce;
    }

    /**
     * Carrega os representantes.
     *
     * @return lista de representantes
     */
    private static List<Representante> carregarReps() {

        List<Representante> listaReps = new ArrayList<>();

        try {
            input = new Scanner(new File(PASTA_FICHEIROS.concat(FICHEIRO_REPS)));
            input.useDelimiter(";");

            while (input.hasNext()) {

                String line = input.next().trim();
                if (line.length() > 0) {

                    String utilizador = line.substring(line.indexOf('-') + 1);
                    String[] atributos = utilizador.split(",");
                    if (atributos.length == 4) {

                        listaReps.add(new Representante(new Utilizador(
                                atributos[0].trim(),
                                atributos[1].trim(),
                                atributos[2].trim(),
                                atributos[3].trim())));
                    }
                }

            }

        } catch (FileNotFoundException ex) {
            System.out.println("ficheiro não encontrado.");
        }

        return listaReps;
    }

    /**
     * Carrega os utilizadores funcionários.
     *
     * @return lista de utlizadores
     */
    private static List<Utilizador> carregarUsersFuncionarios() {

        List<Utilizador> listaUtilizadores = new ArrayList<>();

        try {
            input = new Scanner(new File(PASTA_FICHEIROS.concat(FICHEIRO_USERS_FUNCIONARIOS)));
            input.useDelimiter(";");

            while (input.hasNext()) {

                String line = input.next();
                if (line.length() > 0) {

                    String[] atributos = line.split(",");
                    if (atributos.length == 4) {

                        listaUtilizadores.add(new Utilizador(
                                atributos[0].trim(),
                                atributos[1].trim(),
                                atributos[2].trim(),
                                atributos[3].trim()));
                    }
                }

            }

        } catch (FileNotFoundException ex) {
            System.out.println("ficheiro não encontrado.");
        }

        return listaUtilizadores;
    }

    /**
     * Criar o objeto data apartir da sua representação textual.
     *
     * @param dataString representação textual da data
     * @return objeto data
     */
    private static Data criarData(String dataString) {

        String[] dataElementos = dataString.substring(dataString.indexOf(':') + 1).trim().split("/");

        Data data = new Data();
        if (dataElementos.length == 3) {
            int ano = Integer.parseInt(dataElementos[0]);
            int mes = Integer.parseInt(dataElementos[1]);
            int dia = Integer.parseInt(dataElementos[2]);
            data.setData(ano, mes, dia);
        }
        return data;
    }

    /**
     * Cria o objecto ListaFaes da lista de utilizadores.
     *
     * @param listaFuncionarios lista de utilizadores
     * @param string texto do ficheiro
     * @return lista de faes
     */
    private static ListaFaes criarListaFaes(List<Utilizador> listaFuncionarios, String string) {

        List<Fae> listaFaes = new ArrayList<>();

        String[] listaFaesString = string.split(",");
        for (String faeString : listaFaesString) {

            faeString = faeString.replace("u", "").trim();
            String[] faeAtributos = faeString.split(">");
            try {

                int i = Integer.parseInt(faeAtributos[0].trim()) - 1;
                if (i < listaFuncionarios.size()) {

                    Fae fae = new Fae(listaFuncionarios.get(i));
                    fae.setContCandAvaliadas(Integer.parseInt(faeAtributos[1].trim()));
                    listaFaes.add(fae);
                }

            } catch (Exception ex) {
                System.out.println("erro: Fae não adicionado.");
            }
        }

        return new ListaFaes(listaFaes);
    }

    /**
     * Cria o objecto ListaOrganizadores da lista de utilizadores.
     *
     * @param listaFuncionarios lista de utilizadores
     * @param string texto do ficheiro
     * @return lista de organizadores
     */
    private static ListaOrganizadores criarListaOrganizadores(List<Utilizador> listaFuncionarios, String string) {

        List<Organizador> listaOrgs = new ArrayList<>();

        String[] listaOrgsString = string.split(",");
        for (String orgString : listaOrgsString) {
            orgString = orgString.replace("u", "").trim();
            try {

                int i = Integer.parseInt(orgString) - 1;
                if (i < listaFuncionarios.size()) {

                    listaOrgs.add(new Organizador(listaFuncionarios.get(i)));
                }

            } catch (Exception ex) {
                System.out.println("erro: Organizador não adicionado.");
            }
        }

        return new ListaOrganizadores(listaOrgs);
    }

    /**
     * Cria a candidatura
     *
     * @param candidaturaString representação textual da candidatura
     * @param expo exposição
     * @return candidatura
     */
    private static Candidatura criarCandidatura(String candidaturaString, Exposicao expo) {

        Candidatura candidatura = new Candidatura();

        String[] atributos = candidaturaString.split(",");
        candidatura.setNomeEmpresa(atributos[0].trim());
        candidatura.setMorada(atributos[1].trim());
        candidatura.setTelemovel(atributos[2].trim());
        candidatura.setAreaExpositor(Float.parseFloat(atributos[3].trim()));
        candidatura.setNumeroConvites(Integer.parseInt(atributos[4].trim()));

        String[] demos = atributos[5].substring(atributos[5].indexOf(':') + 1).split("#");
        List<Demonstracao> listaDemos = new ArrayList<>();
        for (String demoString : demos) {

            int index = Integer.parseInt(demoString.trim()) - 1;
            if (index < expo.getListaDemonstracoes().size()) {

                listaDemos.add(expo.getListaDemonstracoes().get(index));
            }
        }
        candidatura.setListaDemonstracoes(listaDemos);

        String[] produtos = atributos[6].substring(atributos[6].indexOf(':') + 1).split("#");
        for (String produtoString : produtos) {

            candidatura.adicionarProduto(new Produto(produtoString.trim()));
        }

        return candidatura;
    }

    /**
     * Carrega a exposição.
     *
     * @param listaFuncionarios lista de utilizadores
     * @param ficheiro ficheiro da exposição
     * @return exposição
     */
    private static Exposicao carregarExposicao(List<Utilizador> listaFuncionarios, File ficheiro) {

        Exposicao expo = new Exposicao();

        try {
            input = new Scanner(ficheiro);
            input.useDelimiter(";");

            while (input.hasNext()) {

                String line = input.next().trim();
                if (line.length() > 0 && line.contains("=")) {

                    String[] campos = line.split("=");
                    switch (campos[0].trim()) {

                        case "dados":

                            String[] atributosExpo = campos[1].split(",");
                            if (atributosExpo.length == 7) {

                                expo.setTitulo(atributosExpo[0].trim());
                                expo.setDescricao(atributosExpo[1].trim());

                                expo.setDataInicio(criarData(atributosExpo[2].trim()));
                                expo.setDataFim(criarData(atributosExpo[3].trim()));
                                expo.setSubInicio(criarData(atributosExpo[4].trim()));
                                expo.setSubFim(criarData(atributosExpo[5].trim()));

                                expo.setLocal(new Local(atributosExpo[6].trim()));
                            }

                            break;
                        case "listaFaes":

                            expo.setListaFaes(criarListaFaes(listaFuncionarios, campos[1]));

                            break;
                        case "listaOrganizadores":

                            expo.setListaOrganizadores(criarListaOrganizadores(listaFuncionarios, campos[1]));

                            break;
                        case "listaDemos":

                            List<Demonstracao> listDemos = new ArrayList<>();
                            String[] listaDemosString = campos[1].split(",");
                            for (String demoString : listaDemosString) {
                                listDemos.add(new Demonstracao(demoString.trim()));
                            }
                            expo.setListaDemonstracoes(listDemos);

                            break;
                        case "listaCandidaturas":

                            List<Candidatura> listaCandidaturas = new ArrayList<>();

                            String[] candidaturasStrings = campos[1].split("&");
                            for (String candidaturaString : candidaturasStrings) {

                                listaCandidaturas.add(criarCandidatura(candidaturaString, expo));
                            }
                            expo.setListaCandidaturas(new ListaCandidaturas(listaCandidaturas));

                            break;
                        default:
                            break;
                    }
                }

            }

        } catch (FileNotFoundException ex) {
            System.out.println("ficheiro não encontrado.");
        }

        return expo;
    }
}
