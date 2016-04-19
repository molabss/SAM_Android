package br.com.constran.mobile.adapter.indicepluviometrico;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.constran.mobile.R;
import br.com.constran.mobile.adapter.AbstractAdapter;
import br.com.constran.mobile.enums.Operacao;
import br.com.constran.mobile.persistence.vo.aprop.indpluv.IndicePluviometricoVO;
import br.com.constran.mobile.view.util.Util;

import java.util.List;

/**
 * @author Mateus Vitali <mateus.vitali@constran.com.br>
 * @version 1.0
 * @since 2014-09-01
 */
public class IndicePluviometricoAdapter extends AbstractAdapter<IndicePluviometricoVO> {

    private final int LIMIT = 24; //tamanho limite do texto do pluviometro

    public IndicePluviometricoAdapter(Context context, List<IndicePluviometricoVO> list) {
        super(context, R.layout.indice_pluviometrico_servico_item, list);
    }

    @Override
    protected void preencherItensGrid(View view, int i) {
        TextView dataTextView = (TextView) view.findViewById(R.id.dataTextView);
        TextView pluviometroTextView = (TextView) view.findViewById(R.id.pluviometroTextView);
        ImageView edicaoImage = (ImageView) view.findViewById(R.id.edicaoImage);
        ImageView deletarImage = (ImageView) view.findViewById(R.id.deletarImage);

        IndicePluviometricoVO indicePluviometricoVO = list.get(i);

        dataTextView.setText(Util.toCompleteHourFormat(indicePluviometricoVO.getData()));
        pluviometroTextView.setText(Util.getInitTxt(Util.toCamelCase(indicePluviometricoVO.getPluviometro()), LIMIT));

        edicaoImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.SELECAO;
                return false;
            }
        });

        deletarImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                operacao = Operacao.EXCLUSAO;
                return false;
            }
        });
    }
}
