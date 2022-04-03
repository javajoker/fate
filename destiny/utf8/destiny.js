(function($){
	var fate = [], ppl = {};
	var renderChart = function(id, data, renderer) {
		var average = 0, deviation = 0;
		for(var i=0;i<data.length;++i) {
			average += data[i][1];
		}
		average /= data.length;
		for(var i=0;i<data.length;++i) {
			deviation += (data[i][1] - average) * (data[i][1] - average);
		}
		deviation /= data.length;
		deviation = Math.sqrt(deviation);

		var data1 = [[data[0][0], average+deviation], [data[data.length-1][0], average+deviation]], 
			data2 = [[data[0][0], average-deviation], [data[data.length-1][0], average-deviation]];

					var plot = $.jqplot(id, [data, data1, data2], { 
//						fillBetween: {
//							series1: 1,
//							series2: 2,
//							color: "rgba(227, 167, 111, 0.7)",
//							fill: true
//						},
						seriesDefaults: {
//							fill: true,
//							fillToZero: true,
							rendererOptions: {
								highlightMouseDown: true,
								smooth: true
							}
						},
						series: [
							{},
							{ 
								color: 'rgba(198,88,88,.6)',
								linePattern: 'dashed' 
							}, { 
								color: 'rgba(198,88,88,.6)',
								linePattern: 'dashed' 
							}
						],
						axes:{
							xaxis:{
								renderer: (renderer ? renderer : $.jqplot.LinearAxisRenderer), 
								rendererOptions: {
									tickInset: 0
								}
							}
						},
						cursor:{
							show: true,
							zoom: true
						}
					});
					return plot;
	};
	var doCalculate = function(y, m, d, h, i, gender, timezone) {
		fate = [];
		ppl = {};
		var param = {
			y: y, m: m, d: d, h: h, i: i,
			gender: gender,
			timezone: timezone
		};
		$.ajax({
			type: "POST",
			url: "json.php",
			data: param,
			success: function(_data, textStatus){
				$('#ie_chart_5e').html('');
				var data = [], ticks = [];
				var host = _data.ele[_data.host], average=max=0, min=100000, maxid=minid=_data.host;
				for(var i=0;i<5;++i) {
					average += _data.ele[i];
					if(_data.ele[i]>max) {max=_data.ele[i];maxid=i;}
					if(_data.ele[i]<min) {min=_data.ele[i];minid=i;}

					data.push((_data.ele[i])/host);
					ticks.push(consts._5[i].name);
				}
				average /= 5;

					var plot = $.jqplot('ie_chart_5e', [data], { 
//						animate: !$.jqplot.use_excanvas,
						seriesDefaults:{
							renderer:$.jqplot.BarRenderer,
							rendererOptions: { fillToZero: true },
							pointLabels: { show: true }
						},
						axes: {
							xaxis: {
								renderer: $.jqplot.CategoryAxisRenderer,
								ticks: ticks
							}
						},
						highlighter: { show: false }
					});
				$('#ie_result').html('<table style="font-size:90%;border:1px solid #aaaaaa;background-color:#f9f9f9;color:black;margin-bottom:0.5em;margin-left:1em;padding:0.2em;">' +
				'<tr><th style="width:10em;">心性</th><td>' + consts._10[_data.pattern].inside + '</td></tr>' +
				'<tr><th>外貌</th><td>' + consts._5[maxid].over + '</td></tr>' +
				'<tr><th>为人</th><td>' + (host>average ? consts._5[_data.host].insideover : consts._5[_data.host].insideunder) + 
					(_data.host != maxid ? '<br/>' + consts._5[maxid].insideover : '') + '</td></tr>' +
				'<tr><th>身体</th><td>' + consts._5[(max-average>average-min)?maxid:minid].organ + '容易病变，日常注意' + consts._5[(max-average>average-min)?maxid:minid].outlook + '的不适和变化</td></tr>' +
				'<tr><th>适宜从事职业</th><td>' + 
						consts._5[_data.god[0]].work + '<br/>' + consts._5[_data.god[1]].work + '</td></tr>' +
				'<tr><th>不适宜从事职业</th><td>' + consts._5[_data.god[2]].work + '</td></tr>' +
				'</table>');
				
				var ret = '';

				if(_data.timespan.length < 2) ret = consts.hint.nothing;

				for(var i=0;i<10;++i) $('#fate' + i).html(ret);
				for(var i=0;i<6;++i) $('#ppl' + i).html(ret);

				if(_data.timespan.length < 2) return;

				for(var id in _data.ppl) {
					var dat = [];
					for(var i=0;i<_data.timespan.length;++i) {
						dat.push([_data.timespan[i], _data.ppl[id][i]]);
					}
					ppl[id] = renderChart('ppl' + id, dat, $.jqplot.DateAxisRenderer);
				}
				
				for(var id=0;id<10;++id) {
					var dat = [];
					for(var i=0;i<_data.timespan.length;++i) {
						dat.push([_data.timespan[i], _data.fate[id][i]]);
					}
					var name = consts._10[id].name;
					fate.push(renderChart('fate' + id, dat, $.jqplot.DateAxisRenderer));
				}
			},
			error: function(_data, textStatus){
				alert(textStatus);
				//$('#ie_result').html(_data.responseText);
			},
			dataType: "json"
		});
	};
	$(document).ready(function(){
		$('#ie_reset').click(function(){
			$( '#datepicker' ).val('2012-01-01');
			$('#ie_chart_5e').html('');
			$('#ie_result').html('');
			for(var i=0;i<10;++i) $('#fate' + i).html('');
			for(var i=0;i<6;++i) $('#ppl' + i).html('');
			$("#accordion").accordion( "activate" , false );
		});
		$('#ie_go').click(function(){
			var ds = $('#datepicker').val().split('-');
			if(ds.length != 3) {
				alert('日期错误（年-月-日）');
				$( '#datepicker' ).val('2012-01-01');
				return;
			}
			var y=parseInt(ds[0]), m=parseInt(ds[1]), d=parseInt(ds[2]);
			if(y==NaN || m==NaN || d==NaN) {
				alert('日期错误（年-月-日）');
				$( '#datepicker' ).val('2012-01-01');
				return;
			}
			$("#accordion").accordion( "activate" , 0 );
			$("#ie_chart_fate").tabs("select", 0)
			$("#ie_chart_ppl").tabs("select", 0)
			doCalculate(
				y, m, d,
				$('#ie_h').val(),
				$('#ie_i').val(),
				($('#ie_gender')[0].checked ? 0 : 1),
				$('#ie_timezone').val()
			);
		});
		
		$("#ie_chart_fate").tabs();
		$('#ie_chart_fate').bind('tabsshow', function(event, ui) {
			var plot = fate[ui.index];
			if (plot && plot._drawCount == 0) {
				plot.replot();
			}
		});
		$("#ie_chart_ppl").tabs();
		$('#ie_chart_ppl').bind('tabsshow', function(event, ui) {
			var plot = ppl[ui.index > 1 ? ui.index + 1 : ui.index];
			if (plot && plot._drawCount == 0) {
				plot.replot();
			}
		});
		$("#accordion").accordion({collapsible:true});
		$("#accordion").accordion( "activate" , false );
		$('#accordion').bind('accordionchange', function(event, ui) {
			var index = $(this).find("h2").index ( ui.newHeader[0] );
			var plot = null;
			if (index == 1) {
				plot = ppl[2];
			} else if (index == 2) {
				plot = fate[0];
			} else if (index == 3) {
				plot = ppl[0];
			}
			if (plot != null && plot._drawCount == 0) {
				plot.replot();
			}
		});
		$( "#datepicker" ).datepicker({
			dateFormat: 'yy-mm-dd',
			minDate: new Date(1910, 1 - 1, 1),
			maxDate: 0,
			changeMonth: true,
			changeYear: true
		});
	})
})(jQuery);