int offsetX = d.width / 2 - (int)ch01.x;
        // マップの端ではスクロールしないようにする
        if(ch01.x<48)offsetX=0;
        if(ch01.x>(bw-110+4))offsetX=0;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, d.width - bw);