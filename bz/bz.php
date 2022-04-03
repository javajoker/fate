<?php
$name=$_POST["name"];$y=$_POST["y"];$m=$_POST["m"];$d=$_POST["d"];$h=$_POST["h"];$i=$_POST["i"];$term1=$_POST["term1"];$term2=$_POST["term2"];$sex=$_POST["sex"];$start_term=$_POST["start_term"];$end_term=$_POST["end_term"];$start_term1=$_POST["start_term1"];$end_term1=$_POST["end_term1"];$cY=$_POST["cY"];$cM=$_POST["cM"];$cD=$_POST["cD"];$cH=$_POST["cH"];$lDate=$_POST["lDate"];
if(!$name or !$y  or  !$m  or  !$d  or  !$name  or  !$term1  or  !$term2  or  !$cY  or  !$cM  or  !$cD  or  !$cH){exit;}
if($sex==0 or $sex==1){}else{exit;}
if($sex==0)$sexn="男";else $sexn="女";$Gan=array("甲","乙","丙","丁","戊","己","庚","辛","壬","癸");
$Zhi=array("子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥");
$Animals=array("鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪");
$wh=array("木","火","土","金","水");
$ys=array("绿","红","黄","白","黑");
$yy=array("阳","阴");
$solarTerm =array("小寒","大寒 ","立春","雨水 ","惊蛰","春分 ","清明","谷雨 ","立夏","小满 ","芒种","夏至 ","小暑","大暑 ","立秋","处暑 ","白露","秋分 ","寒露","霜降 ","立冬","小雪 ","大雪","冬至 ");
$shiShen=array("比劫","比肩","正印","偏印","正官","七杀","伤官","食神","偏财","正财");
$dz=array(9,5,0,1,4,2,3,5,6,7,4,8);
$riGan=array(array(1,0,7,6,8,9,5,4,3,2),array(0,1,6,7,9,8,4,5,2,3),array(3,2,1,0,7,6,8,9,5,4),array(2,3,0,1,6,7,9,8,4,5),array(5,4,3,2,1,0,7,6,8,9),array(4,5,2,3,0,1,6,7,9,8),array(8,9,5,4,3,2,1,0,7,6),array(9,8,4,5,2,3,0,1,6,7),array(7,6,8,9,5,4,3,2,1,0),array(6,7,9,8,4,5,2,3,0,1));function cyclical($num){
    global $Gan,$Zhi;
    return($Gan[$num%10].$Zhi[$num%12]);
}eregi("(.*)/(.*)", $term1,$zc_1);
$zc1 = $solarTerm[$zc_1[1]].$zc_1[2];
eregi("(.*)/(.*)", $term2, $zc_2);
$zc2 = $solarTerm[$zc_2[1]].$zc_2[2];eregi("(.*)/(.*)", $start_term1,$zc_11);
$start_term1 = $solarTerm[$zc_11[1]].$zc_11[2];
eregi("(.*)/(.*)", $end_term1, $zc_22);
$end_term1 = $solarTerm[$zc_22[1]].$zc_22[2];$gzY = cyclical($cY);
$gzM = cyclical($cM);
$gzD = cyclical($cD);
$gzH = cyclical($cH);
$SC = $Zhi[$cH%12];
$y10=$cY%10;
$m10=$cM%10;
$d10=$cD%10;
$h10=$cH%10;
$y12=$cY%12;
$m12=$cM%12;
$d12=$cD%12;
$h12=$cH%12;
$ywh=floor($cY%10/2);
$mwh=floor($cM%10/2);
$dwh=floor($cD%10/2);
$hwh=floor($cH%10/2);
$yyy=$y10%2;
$myy=$m10%2;
$dyy=$f10%2;
$hyy=$h10%2;$wY = $wh[$ywh];
$wM = $wh[$mwh];
$wD = $wh[$dwh];
$wH = $wh[$hwh];
$sY = $ys[$ywh];
$sM = $ys[$mwh];
$sD = $ys[$dwh];
$sH = $ys[$hwh];
$yY = $yy[$y10%2];
$yM = $yy[$m10%2];
$yD = $yy[$d10%2];
$yH = $yy[$h10%2];
$aY = $Animals[$y12];
$aM = $Animals[$m12];
$aD = $Animals[$d12];
$aH = $Animals[$h12];
if($yyy==$sex){
    $termdate=$end_term;
    $dr=$cM+1;
    $dr_gz=cyclical($dr)."".cyclical($dr+1)."&nbsp;".cyclical($dr+2)."".cyclical($dr+3)."&nbsp;".cyclical($dr+4)."&nbsp;".cyclical($dr+5)."".cyclical($dr+6)."&nbsp;".cyclical($dr+7);
    $dr_ss=$shiShen[$riGan[$d10][$dr%10]]."".$shiShen[$riGan[$d10][($dr+1)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr+2)%10]]."".$shiShen[$riGan[$d10][($dr+3)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr+4)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr+5)%10]]."".$shiShen[$riGan[$d10][($dr+6)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr+7)%10]];
}else{
    $termdate=$start_term;
    $dr=$cM - 1;
    $dr_gz=cyclical($dr)."".cyclical($dr-1)."&nbsp;".cyclical($dr-2)."".cyclical($dr-3)."&nbsp;".cyclical($dr-4)."&nbsp;".cyclical($dr-5)."".cyclical($dr-6)."&nbsp;".cyclical($dr-7);
    $dr_ss=$shiShen[$riGan[$d10][$dr%10]]."".$shiShen[$riGan[$d10][($dr-1)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr-2)%10]]."".$shiShen[$riGan[$d10][($dr-3)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr-4)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr-5)%10]]."".$shiShen[$riGan[$d10][($dr-6)%10]]."&nbsp;".$shiShen[$riGan[$d10][($dr-7)%10]];
}$dayday = $start_term/60/60/24; $allday=$termdate/60/12;
$nnnn =  floor($allday/360);
$mmmm = floor(($allday%360)/30);
$dddd =  ceil(($allday%360)%30);$term_old=$mmmm*2+floor(($dayday+$dddd)/30)*2+$zc_11[1];
$term_m= $term_old%24;
$add_y=floor($term_old/24);
$term_mn=$solarTerm[$term_m];
$term_d=ceil($dayday+($allday%360)%30)%30;
$term_m>5?$term_day=8:$term_day=6;
$gl_m=($term_m/2+1+floor(($term_day+$term_d)/30))%12;
$gl_d=($term_day+$term_d)%30;
$dr_y=$y+$nnnn+$add_y;
if($gl_m<2 or ($gl_m==2 and $gl_d<5))$dr_y=$y+$nnnn+$add_y-1;
$dr_cY=$Gan[($dr_y-1900+36)%10];
$dr_cY2=$Gan[($dr_y-1900+36+5)%10];
$dayundate="出生后从".$nnnn."岁".$mmmm."月".$dddd."天上运，逢".$dr_cY."、".$dr_cY2."年的".$term_mn."后第".$term_d."日（公历".$gl_m."月".$gl_d."日前后）交运。
<br>在交运期前后您的命运将有重大的改变和吉凶极端的际遇，凡事宜谨慎!<br>"; 
include("bzdata/yq.php");
$yq_y1=date("Y")-3;
if($dr_y>=$yq_y1) $yq_y1+=10;
if($dr_y>=$yq_y1) $yq_y1+=10;
for($f=$yq_y1; $f<($yq_y1+11); $f++) {
    $yq_s3=$yq_s1;
	if($yyy==$sex)$yq_gz=$dr+floor(($f-$dr_y)/10);
    else  $yq_gz=$dr-floor(($f-$dr_y)/10);
    if((($f-$dr_y)%10)<5)$yq_g=$yq_gz%10;
    else $yq_g=$dz[$yq_gz%12];
    $yq_s1=$riGan[$d10][$yq_g];
    $yq_s2=$riGan[$d10][($f-1900+36)%10];
    if((($f-$dr_y)%5)==0 and ($gl_m>2 or ($gl_m==2 and $gl_d>4))){
        if($yq_s3)$yq_text.="<font color=blue>".$f."年 2月 4日～★".$f."年 ".$gl_m."月 ".$gl_d."日：</font><br>\n".$yq[$yq_s3][$yq_s2]."<br>\n";
        $yq_text.="<font color=blue>"."★".$f."年 ".$gl_m."月 ".$gl_d."日～".($f+1)."年 2月 4日：</font><br>\n".$yq[$yq_s1][$yq_s2]."<br>\n".$ssjs[$yq_s2]."<br><br style=line-height:40%>\n";
    }else{
        $yq_text.="<font color=blue>".$f."年 2月 4日～".($f+1)."年 2月 4日：</font><br>\n".$yq[$yq_s1][$yq_s2]."<br>\n".$ssjs[$yq_s2]."<br><br style=line-height:40%>\n";
    }
} 
$myq_y=$riGan[$d10][($f-1900+36)%10];
$myq_m=($y-1900)*12+$m+12;
$myq_m=$Gan[$myq_m%10];
$myq_m=$iGan[$d10][$myq_m];$myq_m=date("m")-3;
$myq_y=date("Y");
if($myq_m<=0){$myq_y--;$myq_m+=12;}
$myq_mm=array(0,6,4,6,5,5,6,7,8,8,8,7,7);
$b1=$myq_m;
$myq_y1=$myq_y;
for($b=$myq_m; $b<($myq_m+12); $b++) {
    $myq_y3=$myq_y1;
    if($b1>12){$b1=$b1-12;$myq_y3=$myq_y1;$myq_y1++;}
    $b2=$b1+1;
    $myq_y2=$myq_y1;
    if($b2>12){$b2=$b2-12;$myq_y2++;}
    $myq_ys=$riGan[$d10][($myq_y3-1900+36)%10];
    $myq_ms=$riGan[$d10][(($myq_y1-1900)*12+$b1+12)%10];
   
	$myq_text.="<font color=blue>".$myq_y1."年 ".$b1."月 ".$myq_mm[$b1]."日～".$myq_y2."年 ".$b2."月 ".$myq_mm[$b2]."日：</font><br>\n".$yq[$myq_ys][$myq_ms]."<br>\n".$ssjs[$myq_ms]."<br><br style=line-height:40%>\n";
    $b1++;
}
$ganNum=array(array(1.2,1.2,1.0,1.0,1.0,1.0,1.0,1.0,1.2,1.2),array(1.06,1.06,1.0,1.0,1.1,1.1,1.14,1.14,1.1,1.1),array(1.14,1.14,1.2,1.2,1.06,1.06,1.0,1.0,1.0,1.0),array(1.2,1.2,1.2,1.2,1.0,1.0,1.0,1.0,1.0,1.0),array(1.1,1.1,1.06,1.06,1.1,1.1,1.1,1.1,1.04,1.04),array(1.0,1.0,1.14,1.14,1.14,1.14,1.06,1.06,1.06,1.06),array(1.0,1.0,1.2,1.2,1.2,1.2,1.0,1.0,1.0,1.0),array(1.04,1.04,1.1,1.1,1.16,1.16,1.1,1.1,1.0,1.0),array(1.06,1.06,1.0,1.0,1.0,1.0,1.14,1.14,1.2,1.2),array(1.0,1.0,1.0,1.0,1.0,1.0,1.2,1.2,1.2,1.2),array(1.0,1.0,1.04,1.04,1.14,1.14,1.16,1.16,1.06,1.06),array(1.2,1.2,1.0,1.0,1.0,1.0,1.0,1.0,1.14,1.14));
$zhi2ganNum=array(1,3,2,1,3,2,1,3,2,1,3,2);
$zhi2gan=array(array(9),array(9,7,5),array(2,0),array(1),array(1,9,4),array(6,2),array(3),array(3,1,5),array(8,6),array(7),array(7,3,4),array(0,8));
$zhiNum=array(array(1.2,1.1,1,1,1.04,1.06,1,1,1.2,1.2,1.06,1.14),array(0.36,0.33,0.3,0.3,0.312,0.318,0.3,0.3,0.36,0.36,0.318,0.342),array(0.2,0.228,0.2,0.2,0.23,0.212,0.2,0.22,0.228,0.248,0.232,0.2),array(0.5,0.55,0.53,0.5,0.55,0.57,0.6,0.58,0.5,0.5,0.57,0.5),array(0.3,0.3,0.36,0.36,0.318,0.342,0.36,0.33,0.3,0.3,0.342,0.318),array(0.84,0.742,0.798,0.84,0.77,0.7,0.7,0.728,0.742,0.7,0.7,0.84),array(1.2,1.06,1.14,1.2,1.1,1,1,1.04,1.06,1,1,1.2),array(0.36,0.318,0.342,0.36,0.33,0.3,0.3,0.312,0.318,0.3,0.3,0.36),array(0.24,0.22,0.2,0.2,0.208,0.2,0.2,0.2,0.24,0.24,0.212,0.228),array(0.5,0.55,0.53,0.5,0.55,0.6,0.6,0.58,0.5,0.5,0.57,0.5),array(0.3,0.342,0.3,0.3,0.33,0.3,0.3,0.33,0.342,0.36,0.348,0.3),array(0.7,0.7,0.84,0.84,0.742,0.84,0.84,0.798,0.7,0.7,0.728,0.742),array(1,1,1.2,1.2,1.06,1.14,1.2,1.1,1,1,1.04,1.06),array(0.3,0.3,0.36,0.36,0.318,0.342,0.36,0.33,0.3,0.3,0.312,0.318),array(0.24,0.212,0.228,0.24,0.22,0.2,0.2,0.208,0.212,0.2,0.2,0.24),array(0.5,0.55,0.53,0.5,0.55,0.57,0.6,0.58,0.5,0.5,0.57,0.5),array(0.36,0.33,0.3,0.3,0.312,0.318,0.3,0.3,0.36,0.36,0.318,0.342),array(0.7,0.798,0.7,0.7,0.77,0.742,0.7,0.77,0.798,0.84,0.812,0.7),array(1,1.14,1,1,1.1,1.06,1,1.1,1.14,1.2,1.16,1),array(0.3,0.342,0.3,0.3,0.33,0.318,0.3,0.33,0.342,0.36,0.348,0.3),array(0.2,0.2,0.24,0.24,0.212,0.228,0.24,0.22,0.2,0.2,0.208,0.212),array(0.5,0.55,0.53,0.5,0.55,0.57,0.6,0.58,0.5,0.5,0.57,0.5),array(0.36,0.318,0.342,0.36,0.33,0.3,0.3,0.312,0.318,0.3,0.3,0.36),array(0.84,0.77,0.7,0.7,0.728,0.742,0.7,0.7,0.84,0.84,0.724,0.798));$whNum=array(0,0,0,0,0);  $yGanNum=$ganNum[$m12][$y10];
$mGanNum=$ganNum[$m12][$m10];
$dGanNum=$ganNum[$m12][$d10];
$hGanNum=$ganNum[$m12][$h10];$whNum[$ywh]+=$yGanNum;
$whNum[$mwh]+=$mGanNum;
$whNum[$dwh]+=$dGanNum;
$whNum[$hwh]+=$hGanNum;
function zhi2ganSN($y12) {             
    global $zhi2ganNum,$riGan,$d10,$zhi2gan,$zhiNum,$whNum,$m12;
    $yZhiNum=$zhi2ganNum[$y12];
    $yyy=0;$yyx=0;while($yyy<$y12){$yyx+=$zhi2ganNum[$yyy];$yyy++;}
    if($yZhiNum==1){
        $yZhiNum1=$zhiNum[$yyx][$m12];
        $whNum[floor(($zhi2gan[$y12][0])/2)]+=$yZhiNum1;
    }elseif($yZhiNum==2){
       
        $yZhiNum1=$zhiNum[$yyx][$m12];
        $whNum[floor(($zhi2gan[$y12][0])/2)]+=$yZhiNum1;
        $yZhiNum2=$zhiNum[$yyx+1][$m12];
        $whNum[floor(($zhi2gan[$y12][1])/2)]+=$yZhiNum2;
    }else{
        $yZhiNum1=$zhiNum[$yyx][$m12];
        $whNum[floor(($zhi2gan[$y12][0])/2)]+=$yZhiNum1;
        $yZhiNum2=$zhiNum[$yyx+1][$m12];
        $whNum[floor(($zhi2gan[$y12][1])/2)]+=$yZhiNum2;
        $yZhiNum3=$zhiNum[$yyx+2][$m12];
        $whNum[floor(($zhi2gan[$y12][2])/2)]+=$yZhiNum3;
    }
}
zhi2ganSN($y12);zhi2ganSN($m12);zhi2ganSN($d12);zhi2ganSN($h12);
$whNum[0]*=10;$whNum[1]*=10;$whNum[2]*=10;$whNum[3]*=10;$whNum[4]*=10;$wh1="<!--同类：-->";
$wh1Num=0;
$whh1="";
$whn1=0;
$wh2="<!--异类：-->";
$wh2Num=0;
$whh2="";
$whn2=0;
$zzz1=0;
$zzz2=0;function whwh($num) {
    global $riGan,$d10,$wh,$whNum,$wh1,$wh1Num,$wh2,$wh2Num,$whh1,$whn1,$whh2,$whn2,$zzz1,$zzz2;
    if($riGan[$d10][$num*2]<4){
        $wh1.=$wh[$num].$whNum[$num]."; "; $wh1Num+=$whNum[$num];
        if($whNum[$num]<=$whn1){$whh1=$num;$whn1=$whNum[$num];}
        if($zzz1==0){$whh1=$num;$whn1=$whNum[$num];$zzz1=1;}
    }else{
        $wh2.=$wh[$num].$whNum[$num]."; "; $wh2Num+=$whNum[$num];
        if($whNum[$num]<=$whn2){$whh2=$num;$whn2=$whNum[$num];}
        if($zzz2==0){$whh2=$num;$whn2=$whNum[$num];$zzz2=1;}
    }
}
whwh(0);whwh(1);whwh(2);whwh(3);whwh(4);
$wh12Num=$wh1Num-$wh2Num;
if($wh12Num>7)$wh123="八字过硬";
elseif($wh12Num<-7)$wh123="八字过弱";
else $wh123="八字比较平衡";
if($wh12Num>0)$whh=$whh2;
elseif($wh12Num<0)$whh=$whh1;
else {
    if($whh2>$whh1)$whh=$whh1;
    else $whh=$whh2;
}
?> 



<html><head><title>八字专业测试</title> 
<meta http-equiv=content-type content=text/html; charset=GB2312>
<style>
<!--
body,td,a,p,input{font-family:arial,sans-serif;font-size: 13px;}
//-->
</style>
</head>
<body bgcolor=#fdfee0  leftmargin=0 topmargin=0>
<div align=center>
<br>
<big><font color="#FF0080"><strong>八字专业测试 </strong></font></big>
<hr width=80%>
<br>
<table width=80%>
<tr><td width=100%>
<?php
echo"
姓名：".$name."<br>
性别：".$sexn."<br>
出生：公元".$y."年".$m."月".$d."日".$h."时".$i."分(阳历)<br>
农历：".$lDate.$SC."时<br>
当月节气：".$zc1."； 中气：".$zc2."<br>
生辰八字：".$gzY."年 ".$gzM."月 ".$gzD."日 ".$gzH."时<br>";
echo"</td></tr></table><br>";
echo"
<table border=0   cellPadding=2 cellSpacing=1  width=80%>
<tr><td>
<br>
<font color=#FF0080><strong>一、你的八字命盘 </strong></font><br><br style=line-height=50%>
下列是你的八字命盘。你是<font color=#0033FF><strong>".$sD.$aD."</strong></font>，出生於".$sY.$aY."年。 
日天干代表你，所以你是属<font color=0033FF><strong>".$wD."</strong></font>。
</tr></td></table>
<table border=1 width=80%>
<tr height=27>
<td width=25% align=center bgcolor=#ffff80>年 (祖先)</td>
<td width=25% align=center bgcolor=#ffff80>月 (父母)</td>
<td width=25% align=center bgcolor=#ffff80>日 (自己)</td>
<td width=25% align=center bgcolor=#ffff80>时 (子孙)</td>
</tr>
<tr>
<td bgcolor=#ffffff><img src=images/wh".$yyy.$ywh.".gif alt=".$yY.$wY."><img src=images/tg".$y10.".gif alt=".$yY.$wY.">".$yY.$wY."</td>
<td bgcolor=#ffffff><img src=images/wh".$myy.$mwh.".gif alt=".$yM.$wM."><img src=images/tg".$m10.".gif alt=".$yM.$wM.">".$yM.$wM."</td>
<td bgcolor=#ffffff><img src=images/wh".$dyy.$dwh.".gif alt=".$yD.$wD."><img src=images/tg".$d10.".gif alt=".$yD.$wD.">".$yD.$wD."</td>
<td bgcolor=#ffffff><img src=images/wh".$hyy.$hwh.".gif alt=".$yH.$wH."><img src=images/tg".$h10.".gif alt=".$yH.$wH.">".$yH.$wH."</td></tr>
<tr>
<td bgcolor=#ffffff><img src=images/animals".$y12.".gif alt=".$aY."><img src=images/dz".$y12.".gif alt=".$aY.">".$sY.$aY."</td>
<td bgcolor=#ffffff><img src=images/animals".$m12.".gif alt=".$aM."><img src=images/dz".$m12.".gif alt=".$aM.">".$sM.$aM."</td>
<td bgcolor=#ffffff><img src=images/animals".$d12.".gif alt=".$aD."><img src=images/dz".$d12.".gif alt=".$aD.">".$sD.$aD."</td>
<td bgcolor=#ffffff><img src=images/animals".$h12.".gif alt=".$aH."><img src=images/dz".$h12.".gif alt=".$aH.">".$sH.$aH."</td>
</tr>
</td></tr>
</table>
<table  border=0   cellPadding=2 cellSpacing=1  width=80%>
	<tr >
		<td><br>
八字命盘从阴阳干支三合历取得。上排是天干，由五行「金水木火土」轮流排列。下排是地支，用十二生肖顺序排列。十二生肖可转换成五行。
</td></tr></table>
<br><table  border=0   cellPadding=2 cellSpacing=1  width=80%>
<tr><td>
<font color=#FF0080><strong>二、你的五行得分和喜神 </strong></font><br><br style=line-height=50%>
下列算出你命盘中五行的分数
</td></tr></table>
<table border=1 cellPadding=1 cellSpacing=1  width=80%>
<tr><td>五行</td>
<td><img src=images/Wood.gif alt=木> 木 (日主自己)</td>
<td><img src=images/Fire.gif alt=火> 火 (体智外泄)</td>
<td><img src=images/Soil.gif alt=土> 土 (钱财享受)</td>
<td><img src=images/Metal.gif alt=金> 金 (职位压力)</td>
<td><img src=images/Water.gif alt=水> 水 (保护求知)</td></tr>
<tr>
<td bgcolor=#FFFF80 height=25>分数</td>
<td bgcolor=#FFFF80 align=center>".$whNum[0]."</td>
<td bgcolor=#FFFF80 align=center>".$whNum[1]."</td>
<td bgcolor=#FFFF80 align=center>".$whNum[2]."</td>
<td bgcolor=#FFFF80 align=center>".$whNum[3]."</td>
<td bgcolor=#FFFF80 align=center>".$whNum[4]."</td></tr>
</table><table border=0   cellPadding=2 cellSpacing=1  width=80%>
	<tr>
		<td><br>";
echo"八字五行得分情况：<br>";
echo $wh1."   (同类得分：".$wh1Num.")<br>";
echo $wh2."   (异类得分：".$wh2Num.")<br>";
echo"相差:".$wh12Num."综合旺衰得分:".$wh12Num."<font color=0033FF><b>".$wh123."</b></font><br>";
echo"八字喜用神：".$wh123."，八字喜".$wh[$whh]."，<font color=0033FF><b>".$wh[$whh]."</b></font>就是此命的「喜神」。<br>";echo"<br><br>八字论命是在找五行阴阳的平衡。同类和异类得分基本相同时，五行阴阳较平衡，一生较顺利。当同类和异类得分相差过大时，人生八字就过硬或过弱，一生起伏较大。喜神的选择一般从得分最少的哪一类中选取，此类中值最少的五行就是你的「喜神」或称「有用之神」。当「喜神」来自流年或大运时，命盘的五行会较平衡。平衡的五行较不会打架。换言之，不愉快的事会减到最低点，那一年就会较幸运。八字就是从这个平衡理论，去分析人一生的起落。
这里，我们就选<font color=0033FF><b>".$wh[$whh]."</b></font>当做此命的「喜神」。<br>
</td></tr></table><br>
<table border=0   cellPadding=2 cellSpacing=1  width=80%>
	<tr>
		<td><br>
<font color=#FF0080><strong>三、你的大运 </strong></font><br><br style=line-height=50%>";
echo $dayundate."<br>";
echo"大运十神：&nbsp;&nbsp;".$dr_ss."<br>";
echo"<font color=red>大运干支：&nbsp;&nbsp;".$dr_gz."</font><br>";
echo"交运年份： ".$dr_y."".($dr_y+10)."".($dr_y+20)."".($dr_y+30)."".($dr_y+40)."".($dr_y+50)."".($dr_y+60)."".($dr_y+70)."<br>
交运年龄：".($nnnn+1)."".($nnnn+11)."".($nnnn+21)."".($nnnn+31)."&nbsp;&nbsp;".($nnnn+41)."".($nnnn+51)."".($nnnn+61)."".($nnnn+71)."
<br><br>
</td></tr></table><br>
<table border=0   cellPadding=2 cellSpacing=1  width=80%>
<tr><td>
<font color=#FF0080><strong>四、 近十年运气 </strong></font><br><br style=line-height=50%>
</td></tr>
<tr>
	<td>
        ".$yq_text."
</td></tr></table><br>
<table border=0   cellPadding=2 cellSpacing=1  width=80%>
<tr><td>
<font color=#FF0080><strong>五、 近十二月运气 </strong></font><br><br style=line-height=50%>
</td></tr>
<tr>
	<td>
        ".$myq_text."
</td></tr></table> <br>
<table border=0   cellPadding=2 cellSpacing=1  width=80%>
<tr><td>
<font color=#FF0080><strong>六、 对你的忠言 </strong></font><br><br style=line-height=50%>
</td></tr>
<tr>
	<td>
		喜神是<font color=#0033FF><b>".$wh[$whh]."</b></font> 趋吉避凶的忠言<br><br style=line-height=70%>";
        @include("bzdata/whlove".$whh.".txt");echo"
</td></tr></table>
<br><br>";
?>
</body>
</html>