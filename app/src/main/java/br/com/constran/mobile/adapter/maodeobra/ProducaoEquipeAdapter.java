package br.com.constran.mobile.adapter.maodeobra;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.maodeobra.ApropriacaoServicoVO;

import java.util.List;

/**
 * Criado em 10/06/2014
 * Autor: Rafael Takashima (rafael.takashima@constran.com.br)
 */
public class ProducaoEquipeAdapter extends AbstractAdapter<ApropriacaoServicoVO> {

    public ProducaoEquipeAdapter(Context context, List<ApropriacaoServicoVO> list) {
        super(context, R.layout.producao_equipe_item, list);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        TextView servicoTextView = (TextView) view.findViewById(R.id.servicoTextView);
        TextView horarioTextView = (TextView) view.findViewById(R.id.horarioTextView);
        TextView producaoTextView = (TextView) view.findViewById(R.id.producaoTextView);
        ImageView editarImage = (ImageView) view.findViewById(R.id.editarImage);
//        ImageView excluirImage    = (ImageView) view.findViewById(R.id.excluirImage);

        final ApropriacaoServicoVO aps = list.get(i);

        String unidadeMedida = aps.getServico().getUnidadeMedida();

        unidadeMedida = unidadeMedida != null ? unidadeMedida : "";

        horarioTextView.setText(aps.getHoraIni() + " a " + aps.getHoraFim());
        servicoTextView.setText(aps.getServico().getDescricao());
        producaoTextView.setText(aps.getQuantidadeProduzida().toString() + unidadeMedida);

        editarImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EDICAO;
                return false;
            }
        });

//        excluirImage.setVisibility(View.GONE);

    }
}

