<?php

class 八字命评 {
	private $m_pan;
	private $m_birthday;
	private $乾造;
	private $m_location;

	public function __construct($solarDateTime, $乾造 = true, $location = null) {
		$this->m_birthday = $solarDateTime;
		$this->乾造 = $乾造;
		$this->m_location = $location;
		$this->m_pan = new 四柱盘($solarDateTime, $乾造, $location);

		$this->m_pan->运盘();

		global $__VERBOSE;
		if ($__VERBOSE) {
			printf('== 用神喜忌 ==' . "\n");
			printf('	用神：%s' . "\n", $this->用神()->用神()->toString());
			printf('	喜神：%s' . "\n", $this->用神()->喜神()->toString());
			printf('	忌神：%s' . "\n", $this->用神()->忌神()->toString());

			$this->观盘($this->m_pan);
		}
	}

	private $用神 = null;

	public function 用神() {
		if ($this->用神 == null) {
			$eleWeights = $this->盘五行();
			$deviations = array();
			$zs = $this->m_pan->柱数();
			$host = $zs[日序]->干数()->术数()->五行();

			$evs = 五行::values();
			foreach ($evs as $ele) {
				$newEleWeights = $eleWeights; // copy array
				$ws = array( 基础权重, 0 );
				$e = null;

				$e = $host->官();
				$ws[1] = $newEleWeights[$e->ordinal()];
				$ws = 干数::生克2($ws, array( new 术数($ele, 阴阳::$阳), new 术数($e, 阴阳::$阳) ));
				$newEleWeights[$e->ordinal()] = $ws[1];

				$e = $host->克();
				$ws[1] = $newEleWeights[$e->ordinal()];
				$ws = 干数::生克2($ws, array( new 术数($ele, 阴阳::$阳), new 术数($e, 阴阳::$阳) ));
				$newEleWeights[$e->ordinal()] = $ws[1];

				$e = $host->生();
				$ws[1] = $newEleWeights[$e->ordinal()];
				$ws = 干数::生克2($ws, array( new 术数($ele, 阴阳::$阳), new 术数($e, 阴阳::$阳) ));
				$newEleWeights[$e->ordinal()] = $ws[1];

				$e = $host;
				$ws[1] = $newEleWeights[$e->ordinal()];
				$ws = 干数::生克2($ws, array( new 术数($ele, 阴阳::$阳), new 术数($e, 阴阳::$阳) ));
				$newEleWeights[$e->ordinal()] = $ws[1];

				$e = $host->印();
				$ws[1] = $newEleWeights[$e->ordinal()];
				$ws = 干数::生克2($ws, array( new 术数($ele, 阴阳::$阳), new 术数($e, 阴阳::$阳) ));
				$newEleWeights[$e->ordinal()] = $ws[1];

				$deviations[$ele->ordinal()] = $this->getStandardDeviation($newEleWeights);
			}
			$deviation = $this->getStandardDeviation($eleWeights);

			$忌神 = null;
			$eles = array();
			for ($i = 0; $i < 5; ++$i) {
				$minDeviation = 10000000; //Float.MAX_VALUE;
				$idx = -1;
				for ($j = 0; $j < 5; ++$j) {
					if ($deviations[$j] < $minDeviation) {
						$idx = $j;
						$minDeviation = $deviations[$j];
					}
				}
				if ($minDeviation < $deviation) {
					$eles[] = $evs[$idx];
				} else {
					if (count($eles) == 0)
						$eles[] = $evs[$idx];
					else
						$忌神 = $evs[$idx];
				}
				$deviations[$idx] = 10000000; //Float.MAX_VALUE;
			}
			$this->用神 = new 喜用神($eles);
			$this->用神->忌神($忌神);
		}
		return $this->用神;
	}

	private function 盘五行() {
		$eleWeights = $this->m_pan->支五行();
		$zs = $this->m_pan->柱数();
		for ($i = 0; $i < count($zs); ++$i) {
			$gan = $zs[$i]->干数();
			$eleWeights[$gan->术数()->五行()->ordinal()] += $gan->getWeight();
		}

		return $eleWeights;
	}

	private function getSum($weights) {
		$sum = 0;
		foreach ($weights as $w) {
			$sum += $w;
		}

		return $sum;
	}

	private function getAverage($weights) {
		return $this->getSum($weights) / count($weights);
	}

	private function getStandardDeviation($weights) {
		return $this->getDeviation($weights, $this->getAverage($weights));
	}

	private function getDeviation($weights, $average) {
		$deviation = 0;
		foreach ($weights as $w) {
			$deviation += ($w - $average) * ($w - $average);
		}
		$deviation /= count($weights);

		return sqrt($deviation);
	}

	public function 观盘($pan) {
		global $__DEBUG;
		printf('== 盘 ==' . "\n");
		$zs = $pan->柱数();
		$start = $__DEBUG ? 0 : 年序;
		$end = count($zs);

		for ($i = $start; $i < $end; ++$i) {
			$x = $zs[$i]->干数();

			printf('	%s	%s(%s):	%s%s %f	%s 支藏 :',
					$zs[$i]->干支()->纳音(),

					$x->天干()->toString(), $x->术数()->五行()->toString(), $i == 日序 ? '(主)' : $x->神()->toString(),
					$i == 日序 ? '　　' : $this->喜用神($x), $x->getWeight(),

					$zs[$i]->支数()->地支()->toString());

			foreach ($zs[$i]->支数()->藏干数() as $y) {
				printf('	- %s	%s%s	%f', $y->术数()->五行()->toString(),
						$y->神()->toString(), $this->喜用神($y), $y->getWeight());
			}
			printf("\n");
		}
	}

	private function 喜用神(干数 $x) {
		return ($x->术数()->五行() === $this->用神()->用神() ? '(用)' : 
				$x->术数()->五行() === $this->用神()->喜神() ? '(喜)' : 
				$x->术数()->五行() === $this->用神()->忌神() ? '(忌)' : 
				'　　');
	}

	public function 八字() {
		$result = array();
		$outside = $inside = $sickness = $work = array();

		$eleWeights = $this->m_pan->五行局面();
		$inside[] = sprintf('%s', $this->m_pan->心性()->心性());

		// 阴阳互通而五行易失和
		// 五行最强显于外，日主强弱为内，五行不平衡影响即为健康隐患
		$evs = 五行::values();
		$deviation = $this->getStandardDeviation($eleWeights);
		$average = $this->getAverage($eleWeights);
		for ($i = 0; $i < count($eleWeights); ++$i) {
			if ($eleWeights[$i] == 0) continue;
			if (abs($eleWeights[$i] - $average) < $deviation) continue;

			$ele = $evs[$i];
			$inside[] = sprintf('%s', ($eleWeights[$i] > $average) ? $ele->盛者人事() : $ele->衰者人事());
			$outside[] = sprintf('%s', ($eleWeights[$i] > $average) ? $ele->盛者外貌() : $ele->衰者外貌());

			$x = 天干::lookup2($ele, 阴阳::$阴);
			$y = 天干::lookup2($ele, 阴阳::$阳);
			$sickness[] = sprintf('%s、%s容易病变，日常注意%s、%s的不适和变化。',
					$x->脏腑()->toString(), $y->脏腑()->toString(), $x->人体()->toString(), $y->人体()->toString());
		}

		$ele = $this->用神()->用神();
		$work[] = sprintf('宜往%s的方向定居，从事如下方面的职业: %s', $ele->方位(), $ele->职业());
		$ele = $this->用神()->喜神();
		$work[] = sprintf('或者往%s的方向定居，从事如下方面的职业: %s', $ele->方位(), $ele->职业());
		$ele = $this->用神()->忌神();
		$work[] = sprintf('注意不宜往%s的方向定居，从事如下方面的职业: %s', $ele->方位(), $ele->职业());

		$result['inside'] = $inside;
		$result['outside'] = $outside;
		$result['sickness'] = $sickness;
		$result['work'] = $work;

		return $result;
	}

	public function 综合命评() {
		$eleWeights = $this->m_pan->五行局面();
		$e = array();
		for($i = 0;$i<5;++$i) {
			$e[] = (isset($eleWeights[$i]) ? $eleWeights[$i] : 0);
		}

		// 阴阳互通而五行易失和
		// 五行最强显于外，日主强弱为内，五行不平衡影响即为健康隐患
		$zs = $this->m_pan->柱数();
		printf('"host" : %s,' . "\n", $zs[日序]->干数()->术数()->五行()->ordinal());
		printf('"ele" : [%s],' . "\n", implode($e, ','));
		$ele1 = $this->用神()->用神();
		$ele2 = $this->用神()->喜神();
		$ele3 = $this->用神()->忌神();
		printf('"god" : [%s, %s, %s],' . "\n", $ele1->ordinal(), $ele2->ordinal(), $ele3->ordinal());
		printf('"pattern" : %s,' . "\n", $this->m_pan->心性()->ordinal());
	}

	public function 综合命评2() {
		$result = $this->八字();

		printf('"心性" : ["%s"],' . "\n", implode( $result['inside'], '","' ));
		printf('"外貌" : ["%s"],' . "\n", implode( $result['outside'], '","' ));
		printf('"健康" : ["%s"],' . "\n", implode( $result['sickness'], '","' ));
		printf('"家宅工作" : ["%s"],' . "\n", implode( $result['work'], '","' ));
	}

	public function 流年行运() {
		list($timespan, $宫, $神) = $this->_流年行运(
				命运::起运交脱($this->m_birthday, $this->乾造, $this->m_location), 
				命运::大运($this->m_birthday, $this->乾造, $this->m_location)
			);

		printf('"timespan":[' . "\n");
		$s = array();
		foreach($timespan as $t) {
			//$s[] = sprintf('["%s", "%s"]' . "\n", date('Y/n/j', $t[0]->getTime()), date('Y/n/j', $t[1]->getTime()));
			$s[] = sprintf('"%s"', date('Y/n/j', ($t[0]->getTime() + $t[1]->getTime()) / 2));
		}
		printf('%s],' . "\n", implode($s, ','));
		
		printf('"ppl":{' . "\n");
		$s = array();
		$顺 = $this->m_pan->顺();
		for ($i = 月序; $i <= 时序; ++$i) {
			$s[] = sprintf('"%s":[%s]' . "\n", ((($i - 1) * 2) + ($顺 ^ true ? 0 : 1)), implode($宫[$i][0], ','));
			$s[] = sprintf('"%s":[%s]' . "\n", ((($i - 1) * 2) + ($顺 ^ false ? 0 : 1)), implode($宫[$i][1], ','));
		}
		printf('%s},' . "\n", implode($s, ','));
		
		printf('"fate":[' . "\n");
		$s = array();
		$sv = 十神::values();
		foreach ($神 as $key => $vals) {
			$s[] = sprintf('[%s]' . "\n", implode($vals, ','));;
		}
		printf('%s]' . "\n", implode($s, ','));
	}

	private function _流年行运($运, $大运) {
		$timespan = $宫 = $神 = array();
		for ($i = 年序; $i <= 时序; ++$i) {
			$宫[$i][0] = array();
			$宫[$i][1] = array();
		}
		foreach (十神::values() as $key) {
			$神[$key->ordinal()] = array();
		}

		$ld = 农历::TimeToLunar($运);
		$d2 = $运;
		$now = new Date();
		for ($i = 0; $i < count($大运); ++$i) {
			$jn = 0;
			$d1 = $d2;
			$d2 = Date::get($d2->getYear(), 1, 节令::term($d2->getYear(), 3, true));
			if ($d2->getTime() > $d1->getTime()) {
				$this->流年行运命评($大运[$i], $d1, $d2, $jn, $timespan, $宫, $神);
				$d1 = $d2;
			}
			$ld->setYear($ld->getYear() + 1);
			for ($j = 0; $j < 9; ++$j) {
				if ($d1->getYear() > $now->getYear() + 3)
					return array( $timespan, $宫, $神 );
				$d2 = Date::get($d2->getYear() + 1, 1, 节令::term($d2->getYear() + 1, 3, true));
				$this->流年行运命评($大运[$i], $d1, $d2, $jn, $timespan, $宫, $神);
				++$jn;
				$d1 = $d2;
				$ld->setYear($ld->getYear() + 1);
			}
			$d3 = Date::get($d2->getYear() + 1, 1, 节令::term($d2->getYear() + 1, 3, true));
			$d2 = 农历::LunarToTime($ld);
			if ($d2->getTime() > $d3->getTime()) {
				$this->流年行运命评($大运[$i], $d1, $d3, $jn, $timespan, $宫, $神);
				$d1 = $d3;
			}
			$this->流年行运命评($大运[$i], $d1, $d2, $jn, $timespan, $宫, $神);
		}

		return array( $timespan, $宫, $神 );
	}

	private function 流年行运命评($运, $始, $终, $大运经年, &$timespan, &$宫, &$神) {
		$pan2 = new 大运流年盘($this->m_birthday, $this->乾造, $this->m_location, $运, 四柱::年柱($始), $大运经年);
		$pan2->运盘();
		global $__VERBOSE;
		if ($__VERBOSE) {
			printf('---------------------------------------' . "\n");
			printf('	%s - %s' . "\n", $始->toString(), $终->toString());
			$this->观盘($pan2);
		}
		$timespan[] = array( $始, $终 );

		$ozs = $this->m_pan->柱数();
		$zs = $pan2->柱数();
		$_神 = array();
		for ($i = 0; $i < count($zs); ++$i) {
			$gan = $zs[$i]->干数();
			$zhi = $zs[$i]->支数();

			$_神[$gan->神()->ordinal()] += $gan->getWeight();

			foreach ($zhi->藏干数() as $g) {
				$_神[$g->神()->ordinal()] += $g->getWeight();
			}

			if ($i > 时序) continue;
			$宫[$i][0][] = $gan->getWeight() / $ozs[$i]->干数()->getWeight();
			$宫[$i][1][] = $zhi->getWeight() / $ozs[$i]->支数()->getWeight();
		}
		foreach (十神::values() as $key) {
			$神[$key->ordinal()][] = $_神[$key->ordinal()] ? $_神[$key->ordinal()] : 0;
		}
	}

	private function 命评($dates, $宫, $神) {
		$weights = $宫[日序][0];

		$this->宫评($dates, $宫, $weights);
		$this->神评($dates, $神, $weights);
	}

	private function 宫评($dates, $宫, $日主序) {
		#Date marriageInLaw = new Date(birthday->getTime());
		#marriageInLaw->setYear(marriageInLaw->getYear() + 18);

		$zs = $this->m_pan->柱数();
		$weights = $宫[日序][1];
		$newWeights = $this->宫评权重($weights, $zs[日序]->支数()->getWeight(), $日主序);
		if ($this->乾造)
			$indexes = $this->getBottoms($newWeights);
		else
			$indexes = $this->getTops($newWeights);
		printf('	可能婚期' . "\n");
		foreach ($indexes as $i) {
			#if ($dates[$i][1].before(marriageInLaw)) continue;
			printf('		%s -	%s' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString());
		}
		global $__VERBOSE;
		if ($__VERBOSE) {
			for ($i = 0; $i < count($dates); ++$i) {
				printf('		%s - %s		%f' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $newWeights[$i]);
			}
		}

		$weights = $宫[时序][0];
		$newWeights = $this->宫评权重($weights, $zs[时序]->干数()->getWeight(), $日主序);
		if ($this->m_pan->顺())
			$indexes = $this->getTops($newWeights);
		else
			$indexes = $this->getBottoms($newWeights);
		printf('	可能得%s' . "\n", $this->m_pan->宫名(时序, true));
		foreach ($indexes as $i) {
			#if ($dates[$i][1].before(marriageInLaw)) continue;
			printf('		%s -	%s' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString());
		}
		global $__VERBOSE;
		if ($__VERBOSE) {
			for ($i = 0; $i < count($dates); ++$i) {
				printf('		%s - %s		%f' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $newWeights[$i]);
			}
		}

		$weights = $宫[时序][1];
		$newWeights = $this->宫评权重($weights, $zs[时序]->支数()->getWeight(), $日主序);
		if ($this->m_pan->顺())
			$indexes = $this->getTops($newWeights);
		else
			$indexes = $this->getBottoms($newWeights);
		printf('	可能得%s' . "\n", $this->m_pan->宫名(时序, false));
		foreach ($indexes as $i) {
			#if ($dates[$i][1].before(marriageInLaw)) continue;
			printf('		%s -	%s' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString());
		}
		global $__VERBOSE;
		if ($__VERBOSE) {
			for ($i = 0; $i < count($dates); ++$i) {
				printf('		%s - %s		%f' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $newWeights[$i]);
			}
		}

		printf('	======= 灾劫或巨变 =======' . "\n");
		for ($x = 月序; $x <= 时序; ++$x) {
			for ($y = 0; $y < 2; ++$y) {
				$weights = $宫[$x][$y];
				$flag = ($y == 0);
				$newWeights = $this->宫评权重($weights, $flag ? 
						$zs[$x]->干数()->getWeight() : 
						$zs[$x]->支数()->getWeight());
				$indexes = $this->getOverflow($newWeights);
				printf('	%s' . "\n", $this->m_pan->宫名($x, $flag));
				foreach ($indexes as $i) {
					printf('		%s -	%s' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString());
				}
				global $__VERBOSE;
				if ($__VERBOSE) {
					for ($i = 0; $i < count($dates); ++$i) {
						printf('		%s - %s		%f' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $newWeights[$i]);
					}
				}
			}
		}
	}

	private function 宫评权重($weights, $baseWeight, $日主序 = null) {
		if (count($weights) == 0)
			return array();
		
		$zs = $this->m_pan->柱数();
		$newWeights = array();

		for ($i = 0; $i < count($weights); ++$i) {
			$w = $weights[$i];

			if ($日主序 != null) {
				$w = $w * $zs[日序]->干数()->getWeight() / $日主序[$i];
			}
			$newWeights[$i] = $w / $baseWeight;
		}

		return $newWeights;
	}

	private function 神评($dates, $神, $日主序) {
		$sv = 十神::values();
		foreach ($神 as $idx => $weights) {
			$_神 = $sv[$idx];
			if (十神::$NA === $_神) continue;

			if (count($weights) == 0) continue;

			$newWeights = array();
			for ($i = 0; $i < count($dates); ++$i) {
				$newWeights[$i] = $weights[$i] / $日主序[$i];
			}

			printf('	======= %s - %s =======' . "\n", $_神->toString(), $_神->指事());

			$average = $this->getAverage($newWeights);
			$indexes = $this->getOverflow($newWeights);
			foreach ($indexes as $i) {
				if ($newWeights[$i] == 0) continue;
				$评 = $newWeights[$i] > $average ? $_神->旺() : $_神->衰();
				if (!$评) continue;
				printf('		%s -	%s :	%s' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $评);
			}
			global $__VERBOSE;
			if ($__VERBOSE) {
				for ($i = 0; $i < count($dates); ++$i) {
					printf('		%s - %s		%f' . "\n", $dates[$i][0]->toString(), $dates[$i][1]->toString(), $newWeights[$i]);
				}
			}
		}
	}

	// private static _1sigma = .6526f;
	// private static _2sigma = .9544f;
	// private static _3sigma = .9974f;
	private static $_maxSum = 7;

	private function sort($ar) {
		$ret = array();
		for ($i = count($ar) - 1; $i >= 0; --$i) {
			$id = $i;
			for ($j = 0; $j < $i; ++$j) {
				if ($ar[$j] > $ar[$id])
					$id = $j;
			}
			$ret[$i] = $ar[$id];
			$ar[id] = $ar[$i];
		}
		return $ret;
	}

	private function getOverflow($weights) {
		$newWeights = $weights;
		$deviation = $this->getStandardDeviation($newWeights);
		$average = $this->getAverage($newWeights);
		$exts = array();
		for ($i = 1; $i < count($newWeights); ++$i) {
			$min = min($newWeights[$i], $newWeights[$i - 1]);
			if($min == 0) continue;
			if (abs($newWeights[$i - 1] - $average) < $deviation && abs($newWeights[$i] - $average) < $deviation) continue;
			if (max($newWeights[$i], $newWeights[$i - 1]) / $min < 2) continue;
			$exts[] = $i;
		}

		return $exts;
	}

	private function getTops($weights) {
		$newWeights = $weights;
		for ($i = 0; $i < count($newWeights); ++$i) {
			if ($newWeights[$i] > 2)
				$newWeights[$i] = 2;
		}
		$deviation = $this->getDeviation($newWeights, 1);
		$valve = ($deviation > .5) ? .5 : $deviation;
		$tops = array();
		for ($i = 1; $i < count($newWeights) - 1; ++$i) {
			if ($newWeights[$i - 1] < $newWeights[$i] && $newWeights[$i] > $newWeights[$i + 1]) {
				if (abs($newWeights[$i] - 1) > $valve) continue;
				$tops[] = $i;
			}
		}

		$sort = $weights;
		for ($i = count($sort) - 1; $i > 0; --$i) {
			$max = $sort[$i];
			for ($j = 0; $j < $i; ++$j) {
				if ($max < $sort[$j]) {
					$max = $sort[$j];
					$sort[$j] = $sort[$i];
					$sort[$i] = $max;
				}
			}
		}
		for ($x = count($sort) - 1, $sum = 0; $x >= 0 && $sum < self::$_maxSum; --$x) {
			if ($sort[$x] == 2) continue;
			for ($i = 1; $i < count($weights) - 1; ++$i) {
				if ($sort[$x] == $weights[$i]) {
					if ($weights[$i - 1] < $weights[$i]
							&& $weights[$i] > $weights[$i + 1]) {
						$tops[] = $i;
						++ $sum;
						break;
					}
				}
			}
		}

		return $this->sort($tops);
	}

	private function getBottoms($weights) {
		$newWeights = $weights;
		for ($i = 0; $i < count($newWeights); ++$i) {
			if ($newWeights[$i] > 2)
				$newWeights[$i] = 2;
		}
		$deviation = $this->getDeviation($newWeights, 1);
		$valve = ($deviation > .5) ? .5 : $deviation;
		$bottoms = array();
		for ($i = 1; $i < count($newWeights) - 1; ++$i) {
			if ($newWeights[$i - 1] > $newWeights[$i] && $newWeights[$i] < $newWeights[$i + 1]) {
				if (abs($newWeights[$i] - 1) > $valve) continue;
				$bottoms[] = $i;
			}
		}

		$sort = $weights;
		for ($i = count($sort) - 1; $i > 0; --$i) {
			$min = $sort[$i];
			for ($j = 0; $j < $i; ++$j) {
				if ($min > $sort[$j]) {
					$min = $sort[$j];
					$sort[$j] = $sort[$i];
					$sort[$i] = $min;
				}
			}
		}
		for ($x = count($sort) - 1, $sum = 0; $x >= 0 && $sum < self::$_maxSum; --$x) {
			if ($sort[$x] == 0) continue;
			for ($i = 1; $i < count($weights) - 1; ++$i) {
				if ($sort[$x] == $weights[$i]) {
					if ($weights[$i - 1] > $weights[$i] && $weights[$i] < $weights[$i + 1]) {
						$bottoms[] = $i;
						++ $sum;
						break;
					}
				}
			}
		}

		return $this->sort($bottoms);
	}
}
