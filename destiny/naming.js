(function($){
	var getPossibleChinese = function(eleids, tabs_id, accordion_id) {
			$('#' + accordion_id).html('');
			for(var e=0;e<eleids.length;++e) {
				var ele = consts._5[eleids[e]].name, idxhtml = '', tabhtml = '', _tabs_id = tabs_id + e;
				for(var i in chinese._5[ele]) {
					var tabid = _tabs_id + e + '_' + i;
					idxhtml += '<li><a href="#' + tabid + '">' + i + '画</a></li>';
					tabhtml += '<div id="' + tabid + '" style="font-size:20px;letter-spacing:12px;">' + chinese._5[ele][i] + '</div>';
				}
				var html = '<h2><a href="#">' + ele + '</a></h2>' +
	'<div style="display:none;">' + 
	'<div id="' + _tabs_id + '">' + 
	'<ul>' + idxhtml + '</ul>' + 
	tabhtml +
	'</div>' + 
	'</div>';
				$('#' + accordion_id).append(html);
				$("#" + _tabs_id).tabs();
			}
			$('#' + accordion_id).accordion('destroy').accordion({collapsible:true});
			$('#' + accordion_id).accordion( "activate" , false );
	};
	var _ele_data = [], _ticks = [];
	var renderPlot = function(data1, data2) {
					var plot = $.jqplot('ie_chart_5e', [data1, data2], { 
//						animate: !$.jqplot.use_excanvas,
						series:[
							{
								renderer:$.jqplot.BarRenderer, 
								rendererOptions: { 
									barWidth: 25,
									barPadding: -15,
									barMargin: 0
								},
								label: '本命'
							}, 
							{ 
//								rendererOptions: { animation: { show: true } },
								label: '姓名' 
							}
						],
						seriesDefaults:{
							rendererOptions: { 
								fillToZero: true, 
								smooth: true 
							},
//							pointLabels: { show: true }
						},
						legend: { show:true, placement: 'outside' },
						axes: {
							xaxis: {
								renderer: $.jqplot.CategoryAxisRenderer,
								ticks: _ticks
							},
							yaxis: { min: 0 }
						},
						highlighter: { show: false }
					});
					return plot;
	};
	var doCalculate = function(y, m, d, h, i, gender, lastname, timezone, where) {
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
				$('#accordion').show();
				$('#ie_chart_5e').html('');
				_ele_data = [];
				_ticks = [];
				empty = [];
				for(var i=0;i<5;++i) {
					if(_data.ele[i] == 0) empty.push(i);
					_ele_data.push(_data.ele[i]);
					_ticks.push(consts._5[i].name);
				}
				
				renderPlot(_ele_data, _ele_data);
				$('#ie_hostele').html(consts._5[_data.host].name);

				if(empty.length>0) {
					$('#ie_emptyele').accordion();
					getPossibleChinese(empty, 'tabs_empty', 'ie_emptyele');
				} else {
					$('#ie_emptyele').html('<p style="text-align:center;font-size:20px;color:#FE2EF7"><五行齐全></p>');
				}
				getPossibleChinese([_data.god[0], _data.god[1]], 'tabs_good', 'ie_goodele');
				getPossibleChinese([_data.god[2]], 'tabs_bad', 'ie_badele');
			},
			error: function(_data, textStatus){
				alert(textStatus);
			},
			dataType: "json"
		});
	};
	$(document).ready(function(){
		$('#ie_reset').click(function(){
			$( '#datepicker' ).val('2012-01-01');
			$('#ie_chart_5e').html('');
			$('#accordion').hide();
			$('#ie_name').val('');
			$('#ie_update').click();
		});
		$('#ie_go').click(function(){
			$('#ie_name').val('');
			$('#ie_update').click();
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
			doCalculate(
				y, m, d,
				$('#ie_h').val(),
				$('#ie_i').val(),
				($('#ie_gender')[0].checked ? 0 : 1),
				$('#ie_name').val(),
				$('#ie_timezone').val(),
				$('#ie_where')[0].checked ? 0 : 1
			);
		});
		$('#ie_update').click(function(){
				var name=$.trim($('#ie_name').val()), lname = '';
				if(name.length>0) {
					var eles = [];
					for(var i=0;i<name.length;++i) {
						var ele = chinese._char[name.charAt(i)];
						if(typeof(ele) == 'undefined') {
							ele='字库中没有';
						} else {
							eles.push(ele);
						}
						lname += '<span style="color:red;font-size:30px;">' + name.charAt(i) + '</span> ( ' + ele + ' )';
					}
					if(_ele_data.length > 0) {
						var data = _ele_data.slice(0), w = 200/eles.length;
						for(var i=0;i<_ticks.length;++i) {
							var offWeight = 0;
							for(var j=0;j<eles.length;++j) {
								if(_ticks[i] == eles[j]) {
									offWeight += w;
								}
							}
							var a = offWeight;
							a = (data[(i+1)%5] < offWeight) ? data[(i+1)%5] : offWeight;
							data[(i+1)%5] += a / 2;
							offWeight -= a / 4;
							a = (data[(i+2)%5] < offWeight) ? data[(i+2)%5] : offWeight;
							data[(i+2)%5] -= a * 3 / 4;
							offWeight -= a / 2;
							a = (data[(i+3)%5] < offWeight) ? data[(i+3)%5] : offWeight;
							data[(i+3)%5] -= a / 2;
							offWeight -= a * 3 / 4;
							a = (data[(i+4)%5] < offWeight) ? data[(i+4)%5] : offWeight;
							data[(i+4)%5] -= a / 4;
							offWeight += a / 2;
							data[i] += offWeight;
						}
						renderPlot(_ele_data, data).redraw();
					}
				} else {
					lname = '<span style="color:green;font-size:20px;"><请输入名字></span>';
				}
				$('#ie_nameele').html(lname);
		});
		$('#ie_goodele').accordion();
		$('#ie_badele').accordion();

		$( "#datepicker" ).datepicker({
			dateFormat: 'yy-mm-dd',
			minDate: new Date(1910, 1 - 1, 1),
			maxDate: 0,
			changeMonth: true,
			changeYear: true
		});
	})
})(jQuery);