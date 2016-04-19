package br.com.constran.mobile.persistence.dao.aprop.maodeobra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.persistence.dao.aprop.BaseDAO;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.*;
import br.com.constran.mobile.persistence.vo.menu.LocalizacaoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Criado em 06/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class IntegranteEquipeDAO extends BaseDAO<IntegranteEquipeVO> {

    private static final Integer COD_AUSENTE = 19;

    private static final String INTEGRANTE_EQUIPE_COL_EQUIPE = "equipe";
    private static final String INTEGRANTE_EQUIPE_COL_PESSOA = "pessoa";
    private static final String INTEGRANTE_EQUIPE_COL_DATA_INGRESSO = "dataIngresso";
    private static final String INTEGRANTE_EQUIPE_COL_DATA_SAIDA = "dataSaida";

    private static IntegranteEquipeDAO instance;

    private IntegranteEquipeDAO(Context context) {
        super(context, TBL_INTEGRANTES_EQUIPE);
    }

    public static IntegranteEquipeDAO getInstance(Context context) {
        return instance == null ? instance = new IntegranteEquipeDAO(context) : instance;
    }

    @Override
    public List<IntegranteEquipeVO> findAllItems() {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString()));
    }

    /**
     * Busca todos os integrantes
     *
     * @param idEquipe
     * @return
     */
    public List<? extends IntegranteVO> findByEquipe(Integer idEquipe) {
        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
                .append(" where eqt.idEquipe = ? ")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString(), concatArgs(idEquipe)));
    }

    /**
     * Busca todos os integrantes da equipe que não faltaram
     *
     * @param equipe
     * @return
     */
    public List<? extends IntegranteVO> findPresentesByEquipe(EquipeTrabalhoVO equipe, String data) {
        //se a data estiver nula, considera a data atual
        if (data == null) {
            data = Util.getToday();
        }

        StringBuilder query = new StringBuilder();
        query.append("select * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
                .append(" where eqt.idEquipe = ? ")
                .append(" and pes.idPessoal not in ")
                .append(" (select distinct pessoa from ").append(TBL_AUSENCIA)
                .append(" where equipe = eqt.idEquipe and data = ?) ")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString(), concatArgs(equipe.getId(), Util.toSimpleDateFormat(data))));
    }

    /**
     * Busca todos os integrantes da equipe cadastrada naquele local que não faltaram
     *
     * @param eventoEquipe
     * @param local
     * @param equipe
     * @return
     */
    public List<? extends IntegranteVO> findByLocalizacaoAndEquipe(EventoEquipeVO eventoEquipe, LocalizacaoVO local, EquipeTrabalhoVO equipe) {
        if (local == null || local.getId() == null || equipe == null || equipe.getId() == null) {
            return new ArrayList<IntegranteVO>();
        }

        StringBuilder query = new StringBuilder();
        query.append("select distinct * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ")
                .append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
                .append(" where eqt.idEquipe = ? ")
                .append(" and pes.idPessoal not in ")
                .append(" (select distinct pessoa from ").append(TBL_AUSENCIA)
                .append(" where equipe = eqt.idEquipe and data = ?) ")
                .append(" order by pes.nome ");

        String[] args = null;

        if (eventoEquipe != null) {
            query.append(" and eqt.idEquipe in (select distinct eeq.equipe from EventoEquipe eeq where eeq.localizacao = ?)");
            args = concatArgs(equipe.getId(), local.getId(), Util.getToday());
        } else {
            args = concatArgs(equipe.getId(), Util.getToday());
        }

        return bindList(findByQuery(query.toString(), args));
    }

    /**
     * Busca todos os integrantes que pertecem a uma equipe
     * EXCETO os que pertencem a equipe atual (membros fixos)
     * EXCETO integrantes temporarios que já estão alocados a uma equipe e NAO estao marcados como ausentes OU a ausencia nao é o último evento
     * EXCETO integrantes que sejam temporarios e nao tenham nenhum apontamento registrado
     * considerando a data/hora atual
     *
     * @param idEquipe equipe atual
     * @return
     */
    public List<? extends IntegranteVO> findNotInEquipe(Integer idEquipe) {
        StringBuilder query = new StringBuilder();
        query.append(" select * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_EQUIPES_TRABALHO).append(" eqt ").append(" on ieq.equipe = eqt.idEquipe ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ").append(" on ieq.pessoa = pes.idPessoal ")
                .append(" where ieq.equipe <> ? ")
                .append(" AND pes.idPessoal not in (")
                .append("     select pes2.idPessoal from ").append(TBL_INTEGRANTES_TEMP).append(" itp ")
                .append("     inner join pessoal pes2 on itp.pessoa = pes2.idPessoal ")
                .append("     and date(substr(itp.dataSaida,7,4)||'-'||substr(itp.dataSaida,4,2)||'-'||substr(itp.dataSaida,1,2)) >= date('now') ")
                .append("     and (pes2.idPessoal not in ( ")
                .append(queryIntegrantesAusentesDisponiveis())
                .append(" ) ")
                .append(" OR ")
                .append(" pes2.idPessoal NOT IN ")
                .append(" (select pes2.idPessoal from  integrantesTemp  itp ")
                .append(" inner join pessoal pes2 on itp.pessoa = pes2.idPessoal ")
                .append(" where date(substr(itp.dataSaida,7,4)||'-'||substr(itp.dataSaida,4,2)||'-'||substr(itp.dataSaida,1,2)) >= date('now') ")
                .append(" and pes2.idPessoal not in ")
                .append(" ( ")
                .append("       select ifNull(amo.pessoa,pmo.pessoa) from apropriacoes apr ")
                .append("       left join apropriacoesMaoObra amo on amo.apropriacao = apr.idApropriacao and amo.pessoa = pes2.idPessoal ")
                .append("       left join paralisacoesMaoObra pmo on pmo.apropriacao = apr.idApropriacao and pmo.pessoa = pes2.idPessoal ")
                .append("       where date(substr(apr.dataHoraApontamento,7,4)||'-'||substr(apr.dataHoraApontamento,4,2)||'-'||substr(apr.dataHoraApontamento,1,2)) >= date('now') ")
                .append(" )) ")
                .append(" )) ")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString(), concatArgs(idEquipe)));
    }

    /**
     * Busca integrantes disponiveis para serem adicionados em outra equipe
     * Regra: um integrante esta disponivel quando:
     * 1- Nao esta alocado em nenhuma equipe ou
     * 2- quando esta alocado em uma equipe, porem seu ultimo evento foi apontado como ausente no dia
     *
     * @return
     */
    public List<? extends IntegranteVO> findIntegrantesDisponiveis() {
        StringBuilder query = new StringBuilder();
        query.append(" select * from ").append(TBL_INTEGRANTES_EQUIPE).append(" ieq ")
                .append(" inner join ").append(TBL_PESSOAL).append(" pes ")
                .append(" on ieq.pessoa = pes.idPessoal ")
                .append(" WHERE ieq.pessoa NOT IN (")
                .append("   select ieq2.pessoa from integrantesEquipe ieq2 ")
                .append("   inner join equipesTrabalho eqt on eqt.idEquipe = ieq2.equipe ")
                .append("   inner join localizacaoEquipe leq on leq.equipe = eqt.idEquipe ")
                .append("   where  date(substr(leq.dataHora,7,4)||'-'||substr(leq.dataHora,4,2)||'-'||substr(leq.dataHora,1,2)) = date('now')")
                .append("   AND ieq2.pessoa NOT IN ( ")
                .append("       select aus.pessoa from ausencia aus ")
                .append("       where date(substr(aus.data,7,4)||'-'||substr(aus.data,4,2)||'-'||substr(aus.data,1,2)) = date('now') ")
                .append("       and aus.equipe = ieq.equipe ")
                .append("       ) ")
                .append("   ) ")
                .append(" OR ieq.pessoa IN (")
                .append(queryIntegrantesAusentesDisponiveis())
                .append(" ) ")
                .append(" order by pes.nome ");

        return bindList(findByQuery(query.toString()));
    }

    /**
     * Busca integrantes temporarios para serem adicionados na equipe
     * TODO: transformar em query única
     *
     * @param idEquipe
     * @return
     */
    public List<? extends IntegranteVO> findTemporariosDisponiveis(Integer idEquipe) {
        List<IntegranteVO> temporariosDisponiveis = new ArrayList<IntegranteVO>();
        List<? extends IntegranteVO> notInEquipe = findNotInEquipe(idEquipe);
        List<? extends IntegranteVO> integrantesDisponiveis = findIntegrantesDisponiveis();

        if (notInEquipe != null) {
            for (IntegranteVO integrante : notInEquipe) {
                if (integrantesDisponiveis != null) {
                    for (IntegranteVO disponivel : integrantesDisponiveis) {
                        if (integrante.getPessoa().getId().equals(disponivel.getPessoa().getId())) {
                            temporariosDisponiveis.add(disponivel);
                            break;
                        }
                    }
                }
            }
        }

        return temporariosDisponiveis;
    }

    /**
     * Query que busca os integrantes apontados como ausentes que estão disponíveis
     *
     * @return
     */
    private String queryIntegrantesAusentesDisponiveis() {

        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct pmo.pessoa from paralisacoesMaoObra pmo  ")
                .append("         inner join apropriacoes apr on apr.idApropriacao = pmo.apropriacao  ")
                .append("         inner join paralisacoes par on par.idParalisacao = pmo.paralisacao  ")
                .append("         where date(substr(apr.dataHoraApontamento,7,4)||'-'||substr(apr.dataHoraApontamento,4,2)||'-'||substr(apr.dataHoraApontamento,1,2)) = date('now')  ")
                .append("              and par.idParalisacao = " + COD_AUSENTE)
                .append("              and (pmo.horaTermino is null or pmo.horaTermino = '' ")
                .append("                  or pmo.horaTermino <> '' and (time('now') >= time(pmo.horaTermino) OR pmo.pessoa ")
                .append("                         in (                                 ")
                .append("                             select ifnull(amo.pessoa, pmo2.pessoa) from apropriacoes apr2")
                .append("                             left join apropriacoesMaoObra amo on amo.apropriacao = apr2.idApropriacao ")
                .append("                             left join paralisacoesMaoObra pmo2 on pmo2.apropriacao = apr2.idApropriacao ")
                .append("                             where ifNull(time(amo.horaInicio), time(pmo2.horaInicio) ) >= time(pmo.horaTermino) ")
                .append("                         )")
                .append("                      ) ")
                .append("                  ) ");

        return sql.toString();
    }

    /**
     * Verifica se o integrante esta disponivel para ser alocado em outra equipe
     *
     * @param pessoaId
     * @return
     */
    public boolean isIntegranteDisponivel(Integer pessoaId, Integer idEquipe) {
//        List<? extends IntegranteVO> integrantesDisponiveis = findIntegrantesDisponiveis();
        List<? extends IntegranteVO> integrantesDisponiveis = findTemporariosDisponiveis(idEquipe);

        if (integrantesDisponiveis != null && !integrantesDisponiveis.isEmpty()) {
            for (IntegranteVO integrante : integrantesDisponiveis) {
                if (pessoaId.equals(integrante.getPessoa().getId())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IntegranteEquipeVO popularEntidade(Cursor cursor) {
        IntegranteEquipeVO integranteEquipe = new IntegranteEquipeVO();
        EquipeTrabalhoVO equipe = EquipeTrabalhoDAO.popularEntidade(context, cursor);
        PessoalVO pessoa = PessoalDAO.popularEntidade(context, cursor);

        equipe.setId(equipe.getId() == null ? getInt(cursor, INTEGRANTE_EQUIPE_COL_EQUIPE) : equipe.getId());
        pessoa.setId(pessoa.getId() == null ? getInt(cursor, INTEGRANTE_EQUIPE_COL_PESSOA) : pessoa.getId());

        integranteEquipe.setDataIngresso(cursor.getString(cursor.getColumnIndex(INTEGRANTE_EQUIPE_COL_DATA_INGRESSO)));
        integranteEquipe.setDataSaida(cursor.getString(cursor.getColumnIndex(INTEGRANTE_EQUIPE_COL_DATA_SAIDA)));
        integranteEquipe.setEquipe(equipe);
        integranteEquipe.setPessoa(pessoa);

        return integranteEquipe;
    }

    @Override
    public ContentValues bindContentValues(IntegranteEquipeVO integranteEquipe) {
        ContentValues contentValues = new ContentValues();
        EquipeTrabalhoVO equipe = integranteEquipe.getEquipe();
        PessoalVO pessoa = integranteEquipe.getPessoa();

        contentValues.put(INTEGRANTE_EQUIPE_COL_EQUIPE, equipe != null ? equipe.getId() : null);
        contentValues.put(INTEGRANTE_EQUIPE_COL_PESSOA, pessoa != null ? pessoa.getId() : null);
        contentValues.put(INTEGRANTE_EQUIPE_COL_DATA_INGRESSO, integranteEquipe.getDataIngresso());
        contentValues.put(INTEGRANTE_EQUIPE_COL_DATA_SAIDA, integranteEquipe.getDataSaida());

        return contentValues;
    }

    @Override
    public boolean isNewEntity(IntegranteEquipeVO ie) {
        return ie != null && ie.getEquipe() == null || ie.getEquipe().getId() == null;
    }

    @Override
    public Object[] getPkArgs(IntegranteEquipeVO ie) {
        return new Object[]{ie.getEquipe().getId(), ie.getPessoa().getId()};
    }

    @Override
    public String[] getPkColumns() {
        return new String[]{INTEGRANTE_EQUIPE_COL_EQUIPE, INTEGRANTE_EQUIPE_COL_PESSOA};
    }
}
