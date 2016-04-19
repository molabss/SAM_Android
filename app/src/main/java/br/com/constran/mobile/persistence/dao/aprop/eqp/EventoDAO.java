package br.com.constran.mobile.persistence.dao.aprop.eqp;

import android.content.Context;
import android.database.Cursor;
import br.com.constran.mobile.R;
import br.com.constran.mobile.persistence.Query;
import br.com.constran.mobile.persistence.dao.AbstractDAO;
import br.com.constran.mobile.persistence.vo.aprop.eqp.EventoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.ArrayList;
import java.util.List;

public class EventoDAO extends AbstractDAO {

    private static final String ALIAS_ID_SERVICO = "idServico";
    private static final String EVENTO_COL_ID = "id";
    private static final String EVENTO_COL_HORA_INICIO = "horaInicio";
    private static final String EVENTO_COL_HORA_TERMINO = "horaTermino";
    private static final String EVENTO_COL_APROPRIAR = "apropriar";

    private static EventoDAO instance;

    private EventoDAO(Context context) {
        super(context, TBL_EVENTOS_EQUIPAMENTO);
    }

    public static EventoDAO getInstance(Context context) {
        if (instance == null) {
            instance = new EventoDAO(context);
        }

        return instance;
    }

    public Cursor getCursorEventosDia(Integer idEquipamento) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"ee.[apropriacao]", "ee.[equipamento]", "ee.[dataHora]", "ee.[horaInicio]", "ee.[horaInicio] || ' ' || ifnull(ee.[horaTermino], '')", "ifnull(s.[descricao], p.[descricao])"};
        tableJoin = "[eventosEquipamento] ee  ";
        tableJoin += "left join[servicos] s on s.[idServico] = ee.[servico] ";
        tableJoin += "left join[paralisacoes] p on p.[idParalisacao] = ee.[paralisacao] ";
        tableJoin += "join[apropriacoesEquipamento] ae on ee.[dataHora] = ae.[dataHora] ";
        tableJoin += "and ee.[equipamento] = ae.[equipamento] and ae.[apropriacao] = ee.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on ae.[apropriacao] = a.[idApropriacao] ";
        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + Util.getToday() + "'";
        conditions += " and ee.[equipamento] = " + idEquipamento;

        orderBy = "ee.[horaInicio] desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public List<EventoVO> getListEventosDia(Integer idEquipamento) {
        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"ee.[apropriacao]", "ee.[equipamento]", "ee.[dataHora]", "ee.[horaInicio]", "ee.[horaTermino]", "ifnull(s.[descricao], p.[descricao])", "ee.[apropriar]"};
        tableJoin = "[eventosEquipamento] ee  ";
        tableJoin += "left join[servicos] s on s.[idServico] = ee.[servico] ";
        tableJoin += "left join[paralisacoes] p on p.[idParalisacao] = ee.[paralisacao] ";
        tableJoin += "join[apropriacoesEquipamento] ae on ee.[dataHora] = ae.[dataHora] ";
        tableJoin += "and ee.[equipamento] = ae.[equipamento] and ae.[apropriacao] = ee.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on ae.[apropriacao] = a.[idApropriacao] ";
        conditions = "substr(a.[dataHoraApontamento],0, 11) = '" + Util.getToday() + "'";
        conditions += " and ee.[equipamento] = " + idEquipamento;

        orderBy = "ee.[horaInicio] desc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return bindList(getCursor(query));
    }

    public void save(EventoVO pVO) {

        StringBuilder builder = new StringBuilder("");

        if (pVO.getStrId() == null) {

            builder.append(" insert into ");
            builder.append(" [eventosEquipamento] ");
            builder.append(" ([apropriacao], [equipamento], [servico], ");
            builder.append(" [dataHora], [horaInicio], [horaTermino], [estaca],");
            builder.append(" [apropriar], [observacoes], [dataHoraCadastro], [dataHoraAtualizacao],[paralisacao], [componente], [categoria])");
            builder.append("  values ");
            builder.append(" (");
            builder.append(pVO.getIdApropriacao());
            builder.append(",");
            builder.append(pVO.getIdEquipamento());
            builder.append(",");

            if (pVO.getServico() == null) {
                builder.append("null,");
            } else {
                builder.append("");
                builder.append(pVO.getServico().getId());
                builder.append(",");
            }

            if (pVO.getDataHora() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getDataHora());
                builder.append("',");
            }


            if (pVO.getHoraInicio() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraInicio());
                builder.append("',");
            }

            if (pVO.getHoraTermino() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraTermino());
                builder.append("',");
            }

            if (pVO.getEstaca() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getEstaca());
                builder.append("',");
            }

            if (pVO.getApropriar() == null) {
                builder.append("'N',");
            } else {
                builder.append("'");
                builder.append(pVO.getApropriar());
                builder.append("',");
            }

            if (pVO.getObservacoes() == null) {
                builder.append("null,");
            } else {
                builder.append("'");
                builder.append(pVO.getObservacoes());
                builder.append("',");
            }

            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");


            builder.append("'");
            builder.append(Util.getNow());
            builder.append("',");

            if (pVO.getParalisacao() == null) {
                builder.append("null,");
            } else {
                builder.append("");
                builder.append(pVO.getParalisacao().getId());
                builder.append(",");
            }

            if (pVO.getComponente() == null) {
                builder.append("null,null");
            } else {
                builder.append("");
                builder.append(pVO.getComponente().getId());
                builder.append(",");
                if (pVO.getComponente().getCategoria() == null) {
                    builder.append("null");
                } else {
                    builder.append(pVO.getComponente().getCategoria().getId());
                }
            }

            builder.append(");");

        } else {

            builder.append(" update [eventosEquipamento] set ");
            builder.append("  [horaTermino]  = ");
            if (pVO.getHoraInicio() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getHoraTermino());
                builder.append("'");
            }

            builder.append(",  [servico]  = ");
            if (pVO.getServico() != null && pVO.getServico().getId() != null) {
                builder.append(pVO.getServico().getId());
            } else {
                builder.append("null");
            }

            builder.append(", [paralisacao]  = ");

            if (pVO.getParalisacao() != null && pVO.getParalisacao().getId() != null) {
                builder.append(pVO.getParalisacao().getId());
            } else {
                builder.append("null");
            }

            builder.append(", [componente]  = ");

            if (pVO.getComponente() != null && pVO.getComponente().getId() != null) {
                builder.append(pVO.getComponente().getId());
            } else {
                builder.append("null");
            }

            builder.append(" , [estaca] = ");
            if (pVO.getEstaca() == null) {
                builder.append(" null ");
            } else {
                builder.append("'");
                builder.append(pVO.getEstaca());
                builder.append("'");
            }

            builder.append(" , [apropriar]  = ");
            builder.append("'");
            builder.append(pVO.getApropriar());
            builder.append("'");
            builder.append(" , [observacoes]  = ");
            builder.append("'");
            builder.append(pVO.getObservacoes());
            builder.append("'");
            builder.append(" , [dataHoraAtualizacao]  = ");
            builder.append("'");
            builder.append(Util.getNow());
            builder.append("'");


            builder.append(" where [apropriacao] = ");
            builder.append(pVO.getIdApropriacao());
            builder.append(" and [equipamento]  = ");
            builder.append(pVO.getIdEquipamento());
            builder.append(" and [horaInicio]  = ");
            builder.append("'");
            builder.append(pVO.getHoraInicio());
            builder.append("'");
            builder.append(" and [dataHora]  = ");
            builder.append("'");
            builder.append(pVO.getDataHora());
            builder.append("';");

        }

        execute(builder);
    }


    public Cursor getCursor(Object[] params) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"trim(e.[prefixo]) as modelo", "trim(ifnull(s.[descricao], p.[descricao])) as evento"
                , "ee.[horaInicio] || ' ' || ee.[horaTermino] as horario",
                " case when ee.[apropriar] = 'Y' then 'Sim' when ee.[apropriar] = 'N' then '" + getStr(R.string.NAO) + "' END as apropriar"};

        tableJoin = "[eventosEquipamento] ee  ";
        tableJoin += "join[equipamentos] e on e.[idEquipamento] = ee.[equipamento] ";
        tableJoin += "left join[servicos] s on s.[idServico] = ee.[servico] ";
        tableJoin += "left join[paralisacoes] p on p.[idParalisacao] = ee.[paralisacao] ";
        tableJoin += "left join[componentes] c on c.[idComponente] = ee.[componente] ";

        conditions = " substr(ee.[dataHora],0, 11) = '" + String.valueOf(params[0]) + "'";

        if (params[1] != null) {
            conditions += " and ee.[equipamento] = " + Integer.valueOf(String.valueOf(params[1]));
        }

        orderBy = "modelo asc, evento asc, horario asc";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        return getCursor(query);
    }

    public String[] getValues(String[] pParams) {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{
                "ifnull(e.[prefixo], '') || ' - ' || ifnull(e.[descricao], '') equipamento",
                " s.[descricao] servico",
                "p.[codigo] || ' - ' || p.[descricao] paralisacao",
                " c.[descricao] componente",
                " ee.[horaInicio] horaInicio",
                " ee.[horaTermino] horaTermino",
                " ee.[apropriar] apropriar",
                " ee.[observacoes] observacoes",
                " ee.[estaca] estaca",
                " p.[codigo] idParalisacao",
                " p.[requerEstaca] requerEstaca",
                " s.[idServico] idServico"
        };

        tableJoin = "[eventosEquipamento] ee  ";
        tableJoin += "left join[equipamentos] e on e.[idEquipamento] = ee.[equipamento] ";
        tableJoin += "left join[servicos] s on s.[idServico] = ee.[servico] ";
        tableJoin += "left join[paralisacoes] p on p.[idParalisacao] = ee.[paralisacao] ";
        tableJoin += "left join[componentes] c on c.[idComponente] = ee.[componente] ";
        tableJoin += "join[apropriacoesEquipamento] ae on ee.[dataHora] = ae.[dataHora] ";
        tableJoin += "and ee.[equipamento] = ae.[equipamento] and ae.[apropriacao] = ee.[apropriacao] ";
        tableJoin += "join [apropriacoes] a on ae.[apropriacao] = a.[idApropriacao] ";

        conditions = " ee.[apropriacao] = " + pParams[0];
        conditions += " and ee.[equipamento] = " + pParams[1];
        conditions += " and ee.[dataHora]  = '" + pParams[2] + "'";
        conditions += " and ee.[horaInicio]  = '" + pParams[3] + "'";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = preencherDados(cursor);

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return dados;
    }


    public String[] getArrayDatas() {

        String columns[] = null;
        String conditions = null;
        String tableJoin = null;
        String orderBy = null;

        columns = new String[]{"substr(ee.[dataHora],0, 11)"};

        tableJoin = " [eventosEquipamento] ee ";
        tableJoin += " join [apropriacoes] a on a.[idApropriacao] = ee.[apropriacao]  ";

        Query query = new Query(true);

        query.setColumns(columns);
        query.setTableJoin(tableJoin);
        query.setConditions(conditions);
        query.setOrderBy(orderBy);

        Cursor cursor = getCursor(query);

        String[] dados = new String[cursor.getCount()];

        int i = 0;

        while (cursor.moveToNext()) {
            dados[i++] = cursor.getString(0);
        }

        return dados;
    }

    @Override
    protected String[] getColunas() {
        return new String[]{
                ALIAS_EQUIPAMENTO,
                ALIAS_SERVICO,
                ALIAS_PARALISACAO,
                ALIAS_COMPONENTE,
                ALIAS_HORA_INI,
                ALIAS_HORA_FIM,
                ALIAS_APROPRIAR,
                ALIAS_OBS,
                ALIAS_ESTACA,
                ALIAS_ID_PARALISACAO,
                ALIAS_REQUER_ESTACA,
                ALIAS_ID_SERVICO
        };
    }


    private List<EventoVO> bindList(Cursor cursor) {
        List<EventoVO> list = new ArrayList<EventoVO>();

        while (cursor != null && cursor.moveToNext()) {
            list.add(popularEntidade(cursor));
        }

        closeCursor(cursor);

        return list;
    }

    public EventoVO popularEntidade(Cursor cursor) {
        EventoVO evento = new EventoVO(getString(cursor, EVENTO_COL_ID), getStr(R.string.TOKEN));
        evento.setHoraInicio(getString(cursor, EVENTO_COL_HORA_INICIO));
        evento.setHoraTermino(getString(cursor, EVENTO_COL_HORA_TERMINO));
        evento.setHoraTermino(getString(cursor, EVENTO_COL_HORA_TERMINO));
        evento.setApropriar(getString(cursor, EVENTO_COL_APROPRIAR));

        return evento;
    }
}
