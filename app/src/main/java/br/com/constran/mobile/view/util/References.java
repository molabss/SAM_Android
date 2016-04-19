package br.com.constran.mobile.view.util;

import br.com.constran.mobile.R;

public class References {

    //---------------------------------------------------------------------------------------------------------------------

    // 	PARAMETROS DO DETAIL

    //---------------------------------------------------------------------------------------------------------------------

    public static final Integer DETAIL_ID_MENU = R.id.layoutDetailMenu;
    public static final Integer DETAIL_ID_LOCAL = R.id.layoutDetailEditLcl;
    public static final Integer DETAIL_ID_CONF = R.id.layoutDetailEditConf;
    public static final Integer DETAIL_ID_ABS = R.id.layoutDetailEditAbs;
    public static final Integer DETAIL_ID_VGS_EQP = R.id.layoutDetailVgsEqp;
    public static final Integer DETAIL_ID_EVT_EQP = R.id.layoutDetailEvtEqp;
    public static final Integer DETAIL_ID_EQP = R.id.layoutDetailEqp;
    public static final Integer DETAIL_ID_EDT_EQP = R.id.layoutDetailEditMov;


    public static final Integer DETAIL_LAYOUT = R.layout.default_detail;

    public static final Integer[] DETAIL_ID_COLUMNS = new Integer[]{R.id.columnDetail1};

//---------------------------------------------------------------------------------------------------------------------

    // 	PARAMETROS DO CABECALHO DA GRID - TOPO

    //---------------------------------------------------------------------------------------------------------------------

    public static final Integer TOP_HEADER_ID_VEQ = R.id.tableHeaderVeqTop;
    public static final Integer TOP_HEADER_ID_EVT = R.id.tableHeaderEvtTop;
    public static final Integer TOP_HEADER_ID_VSC = R.id.tableHeaderVscTop;
    public static final Integer TOP_HEADER_ID_ABS = R.id.tableHeaderAbsTop;
    public static final Integer TOP_HEADER_ID_ABS6 = R.id.tableHeaderAbs6Top;
    public static final Integer TOP_HEADER_ID_ABS7 = R.id.tableHeaderAbs7Top;
    public static final Integer TOP_HEADER_ID_ABS8 = R.id.tableHeaderAbs8Top;
    public static final Integer TOP_HEADER_ID_ABS9 = R.id.tableHeaderAbs9Top;
    public static final Integer TOP_HEADER_ID_ESC = R.id.tableHeaderEscTop;
    public static final Integer TOP_HEADER_ID_LCL = R.id.tableHeaderlclTop;

    public static final Integer TOP_HEADER_LAYOUT_VEQ = R.layout.viagens_equipamento_top;
    public static final Integer TOP_HEADER_LAYOUT_EVT = R.layout.eventos_equipamento_top;
    public static final Integer TOP_HEADER_LAYOUT_VSC = R.layout.viagens_search_top;
    public static final Integer TOP_HEADER_LAYOUT_ABS = R.layout.abastecimentos_search_top;
    public static final Integer TOP_HEADER_LAYOUT_ABS6 = R.layout.abastecimentos_lubrificacao_top;
    public static final Integer TOP_HEADER_LAYOUT_ABS7 = R.layout.abastecimentos_posto_top;
    public static final Integer TOP_HEADER_LAYOUT_ABS8 = R.layout.abastecimentos_saldo_top;
    public static final Integer TOP_HEADER_LAYOUT_ABS9 = R.layout.abastecimento_temp_top;
    public static final Integer TOP_HEADER_LAYOUT_ESC = R.layout.eventos_search_top;
    public static final Integer TOP_HEADER_LAYOUT_LCL = R.layout.local_top;

    public static final Integer[] TOP_HEADER_ID_COLUMN = new Integer[]{R.id.columnHeaderTop1};
    public static final Integer[] TOP_HEADER_ID_COLUMNS = new Integer[]{R.id.columnHeaderTop1, R.id.columnHeaderTop2};


    //---------------------------------------------------------------------------------------------------------------------

    // 	PARáMETROS DO CABEáALHO DA GRID / TABELA

    //---------------------------------------------------------------------------------------------------------------------

    public static final Integer HEADER_ID_MOV = R.id.tableHeaderEqp;
    public static final Integer HEADER_ID_EQP = R.id.tableHeaderEqp;
    public static final Integer HEADER_ID_PFX = R.id.tableHeaderPfx;
    public static final Integer HEADER_ID_VEQ = R.id.tableHeaderVeq;

    public static final Integer HEADER_ID_EVT = R.id.tableHeaderEvt;
    public static final Integer HEADER_ID_VSC = R.id.tableHeaderVsc;
    public static final Integer HEADER_ID_ABS = R.id.tableHeaderAbs;
    public static final Integer HEADER_ID_ABS2 = R.id.tableHeaderEqp;
    public static final Integer HEADER_ID_ABS3 = R.id.tableHeaderEqp;
    public static final Integer HEADER_ID_ABS4 = R.id.tableHeaderEqp;
    public static final Integer HEADER_ID_ABS5 = R.id.tableHeaderAbs5;
    public static final Integer HEADER_ID_ABS6 = R.id.tableHeaderAbs6;
    public static final Integer HEADER_ID_ABS7 = R.id.tableHeaderAbs7;
    public static final Integer HEADER_ID_ABS8 = R.id.tableHeaderAbs8;
    public static final Integer HEADER_ID_ABS9 = R.id.tableHeaderAbs9;
    public static final Integer HEADER_ID_ESC = R.id.tableHeaderEsc;
    public static final Integer HEADER_ID_LCL = R.id.tableHeaderlcl;

    public static final Integer HEADER_LAYOUT_PFX = R.layout.prefixos_header;
    public static final Integer HEADER_LAYOUT_MOV = R.layout.movimentacoes_diarias_header;
    public static final Integer HEADER_LAYOUT_EQP = R.layout.partes_diarias_header;
    public static final Integer HEADER_LAYOUT_VEQ = R.layout.viagens_equipamento_header;
    public static final Integer HEADER_LAYOUT_EVT = R.layout.eventos_equipamento_header;
    public static final Integer HEADER_LAYOUT_VSC = R.layout.viagens_search_header;
    public static final Integer HEADER_LAYOUT_ABS = R.layout.abastecimentos_search_header;
    public static final Integer HEADER_LAYOUT_ABS2 = R.layout.abastecimentos_header;
    public static final Integer HEADER_LAYOUT_ABS3 = R.layout.lubrificacao_header;
    public static final Integer HEADER_LAYOUT_ABS4 = R.layout.abastecimentos_confirm_header;
    public static final Integer HEADER_LAYOUT_ABS5 = R.layout.abastecimentos_preventiva_header;
    public static final Integer HEADER_LAYOUT_ABS6 = R.layout.abastecimentos_lubrificacao_header;
    public static final Integer HEADER_LAYOUT_ABS7 = R.layout.abastecimentos_posto_header;
    public static final Integer HEADER_LAYOUT_ABS8 = R.layout.abastecimentos_saldo_header;
    public static final Integer HEADER_LAYOUT_ABS9 = R.layout.abastecimento_temp_header;
    public static final Integer HEADER_LAYOUT_ESC = R.layout.eventos_search_header;
    public static final Integer HEADER_LAYOUT_LCL = R.layout.local_header;

    public static final Integer[] HEADER_ID_COLUMNS_MOV = new Integer[]{R.id.columnHeaderEqp1};
    public static final Integer[] HEADER_ID_COLUMNS_EQP = new Integer[]{R.id.columnHeaderEqp1};
    public static final Integer[] HEADER_ID_COLUMNS_PFX = new Integer[]{R.id.columnHeaderPfx1, R.id.columnHeaderPfx2, R.id.columnHeaderPfx3};
    public static final Integer[] HEADER_ID_COLUMNS_VEQ = new Integer[]{R.id.columnHeaderVeq1, R.id.columnHeaderVeq2, R.id.columnHeaderVeq3, R.id.columnHeaderVeq4};
    public static final Integer[] HEADER_ID_COLUMNS_EVT = new Integer[]{R.id.columnHeaderEvt1, R.id.columnHeaderEvt2, R.id.columnHeaderEvt3};
    public static final Integer[] HEADER_ID_COLUMNS_VSC = new Integer[]{R.id.columnHeaderVsc1, R.id.columnHeaderVsc2, R.id.columnHeaderVsc3, R.id.columnHeaderVsc4, R.id.columnHeaderVsc5, R.id.columnHeaderVsc6, R.id.columnHeaderVsc7};
    public static final Integer[] HEADER_ID_COLUMNS_ABS = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3, R.id.columnHeaderAbs4, R.id.columnHeaderAbs5};
    public static final Integer[] HEADER_ID_COLUMNS_ABS2 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3};
    public static final Integer[] HEADER_ID_COLUMNS_ABS3 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3, R.id.columnHeaderAbs4};
    public static final Integer[] HEADER_ID_COLUMNS_ABS4 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2};
    public static final Integer[] HEADER_ID_COLUMNS_ABS5 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2};
    public static final Integer[] HEADER_ID_COLUMNS_ABS6 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3, R.id.columnHeaderAbs4};
    public static final Integer[] HEADER_ID_COLUMNS_ABS7 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3, R.id.columnHeaderAbs4};
    public static final Integer[] HEADER_ID_COLUMNS_ABS8 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3};
    public static final Integer[] HEADER_ID_COLUMNS_ABS9 = new Integer[]{R.id.columnHeaderAbs1, R.id.columnHeaderAbs2, R.id.columnHeaderAbs3, R.id.columnHeaderAbs4};
    public static final Integer[] HEADER_ID_COLUMNS_ESC = new Integer[]{R.id.columnHeaderEsc1, R.id.columnHeaderEsc2, R.id.columnHeaderEsc3, R.id.columnHeaderEsc4, R.id.columnHeaderEsc5};
    public static final Integer[] HEADER_ID_COLUMNS_LCL = new Integer[]{R.id.columnlclHeader1, R.id.columnlclHeader2, R.id.columnlclHeader3};


    //---------------------------------------------------------------------------------------------------------------------

    // 	PARáMETROS DA GRID (LISTA)

    //---------------------------------------------------------------------------------------------------------------------

    public static final Integer GRID_ID_PFX = R.id.tableGridPfx;
    public static final Integer GRID_ID_MOV = R.id.tableGridMov;
    public static final Integer GRID_ID_ABS2 = R.id.tableGridMov;
    public static final Integer GRID_ID_ABS4 = R.id.tableGridMov;
    public static final Integer GRID_ID_ABS3 = R.id.tableGridMov;
    public static final Integer GRID_ID_EQP = R.id.tableGridEqp;
    public static final Integer GRID_ID_VEQ = R.id.tableGridVeq;
    public static final Integer GRID_ID_EVT = R.id.tableGridEvt;
    public static final Integer GRID_ID_VSC = R.id.tableGridVsc;
    public static final Integer GRID_ID_ABS = R.id.tableGridAbs;
    public static final Integer GRID_ID_ABS5 = R.id.tableGridAbs5;
    public static final Integer GRID_ID_ABS6 = R.id.tableGridAbs6;
    public static final Integer GRID_ID_ABS7 = R.id.tableGridAbs7;
    public static final Integer GRID_ID_ABS8 = R.id.tableGridAbs8;
    public static final Integer GRID_ID_ABS9 = R.id.tableGridAbs9;
    public static final Integer GRID_ID_ESC = R.id.tableGridEsc;
    public static final Integer GRID_ID_LCL = R.id.tableGridlcl;

    public static final Integer GRID_LAYOUT_MOV = R.layout.movimentacoes_diarias_row;
    public static final Integer GRID_LAYOUT_EQP = R.layout.partes_diarias_row;
    public static final Integer GRID_LAYOUT_PFX = R.layout.prefixos_row;
    public static final Integer GRID_LAYOUT_VEQ = R.layout.viagens_equipamento_row;
    public static final Integer GRID_LAYOUT_EVT = R.layout.eventos_equipamento_row;
    public static final Integer GRID_LAYOUT_VSC = R.layout.viagens_search_row;
    public static final Integer GRID_LAYOUT_ABS = R.layout.abastecimentos_search_row;
    public static final Integer GRID_LAYOUT_ABS2 = R.layout.abastecimentos_row;
    public static final Integer GRID_LAYOUT_ABS3 = R.layout.detalhes_lubrificacao_row;
    public static final Integer GRID_LAYOUT_ABS4 = R.layout.abastecimentos_confirm_row;
    public static final Integer GRID_LAYOUT_ABS5 = R.layout.abastecimentos_preventiva_row;
    public static final Integer GRID_LAYOUT_ABS6 = R.layout.abastecimentos_lubrificacao_row;
    public static final Integer GRID_LAYOUT_ABS7 = R.layout.abastecimentos_posto_row;
    public static final Integer GRID_LAYOUT_ABS8 = R.layout.abastecimentos_saldo_row;
    public static final Integer GRID_LAYOUT_ABS9 = R.layout.abastecimento_temp_row;
    public static final Integer GRID_LAYOUT_ESC = R.layout.eventos_search_row;
    public static final Integer GRID_LAYOUT_LCL = R.layout.local_row;

    public static final Integer[] GRID_ID_COLUMNS_MOV = new Integer[]{R.id.columnEqp1, R.id.columnEqp2};
    public static final Integer[] GRID_ID_COLUMNS_EQP = new Integer[]{R.id.columnEqp1, R.id.columnEqp2, R.id.columnEqp3};
    public static final Integer[] GRID_ID_COLUMNS_PFX = new Integer[]{R.id.columnPfx1};
    public static final Integer[] GRID_ID_COLUMNS_VEQ = new Integer[]{R.id.columnVeq1, R.id.columnVeq2, R.id.columnVeq3, R.id.columnVeq4};
    public static final Integer[] GRID_ID_COLUMNS_EVT = new Integer[]{R.id.columnEvt1, R.id.columnEvt2, R.id.columnEvt3};
    public static final Integer[] GRID_ID_COLUMNS_VSC = new Integer[]{R.id.columnVsc1, R.id.columnVsc2, R.id.columnVsc3, R.id.columnVsc4, R.id.columnVsc5, R.id.columnVsc6, R.id.columnVsc7};
    public static final Integer[] GRID_ID_COLUMNS_ABS = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3, R.id.columnAbs4, R.id.columnAbs5};
    public static final Integer[] GRID_ID_COLUMNS_ABS2 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3, R.id.columnAbs4};
    public static final Integer[] GRID_ID_COLUMNS_ABS3 = new Integer[]{R.id.editAbs1, R.id.editAbs2, R.id.editAbs3};
    public static final Integer[] GRID_ID_COLUMNS_ABS4 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2};
    public static final Integer[] GRID_ID_COLUMNS_ABS5 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2};
    public static final Integer[] GRID_ID_COLUMNS_ABS6 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3, R.id.columnAbs4};
    public static final Integer[] GRID_ID_COLUMNS_ABS7 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3, R.id.columnAbs4, R.id.columnAbs5, R.id.columnAbs6};
    public static final Integer[] GRID_ID_COLUMNS_ABS8 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3};
    public static final Integer[] GRID_ID_COLUMNS_ABS9 = new Integer[]{R.id.columnAbs1, R.id.columnAbs2, R.id.columnAbs3, R.id.columnAbs4};
    public static final Integer[] GRID_ID_COLUMNS_ESC = new Integer[]{R.id.columnEsc1, R.id.columnEsc2, R.id.columnEsc3, R.id.columnEsc4, R.id.columnEsc5};
    public static final Integer[] GRID_ID_COLUMNS_LCL = new Integer[]{R.id.columnlcl1, R.id.columnlcl2, R.id.columnlcl3};

    //---------------------------------------------------------------------------------------------------------------------

    // 	PARáMETROS DO RODAPá DA GRID

    //---------------------------------------------------------------------------------------------------------------------

    public static final Integer FOOTER_ID_MOV = R.id.tableFooterEqp;
    public static final Integer FOOTER_ID_EQP = R.id.tableFooterEqp;
    public static final Integer FOOTER_ID_PFX = R.id.tableFooterPfx;
    public static final Integer FOOTER_ID_VEQ = R.id.tableFooterVeq;
    public static final Integer FOOTER_ID_EVT = R.id.tableFooterEvt;
    public static final Integer FOOTER_ID_VSC = R.id.tableFooterVsc;
    public static final Integer FOOTER_ID_ABS = R.id.tableFooterAbs;
    public static final Integer FOOTER_ID_ABS5 = R.id.tableFooterAbs5;
    public static final Integer FOOTER_ID_ABS6 = R.id.tableFooterAbs6;
    public static final Integer FOOTER_ID_ABS7 = R.id.tableFooterAbs7;
    public static final Integer FOOTER_ID_ABS8 = R.id.tableFooterAbs8;
    public static final Integer FOOTER_ID_ABS9 = R.id.tableFooterAbs9;

    public static final Integer FOOTER_ID_ESC = R.id.tableFooterEsc;
    public static final Integer FOOTER_ID_LCL = R.id.tableFooterlcl;
    public static final Integer FOOTER_LAYOUT_DEFAULT = R.layout.default_footer;
    public static final Integer FOOTER_LAYOUT_PFX = R.layout.prefixos_footer;
    public static final Integer FOOTER_LAYOUT_VEQ = R.layout.viagens_equipamento_footer;
    public static final Integer FOOTER_LAYOUT_EVT = R.layout.eventos_equipamento_footer;
    public static final Integer FOOTER_LAYOUT_VSC = R.layout.viagens_search_footer;
    public static final Integer FOOTER_LAYOUT_ABS = R.layout.abastecimento_search_footer;
    public static final Integer FOOTER_LAYOUT_ESC = R.layout.eventos_search_footer;
    public static final Integer FOOTER_LAYOUT_LCL = R.layout.local_footer;

    public static final Integer[] FOOTER_ID_COLUMNS = new Integer[]{R.id.columnFooter};

}

