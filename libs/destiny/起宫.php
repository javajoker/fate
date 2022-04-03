<?php

class 起宫 {
	public static function 胎元($月柱) {
		return 干支::lookup($月柱->术数()->getIndex() - 9);
	}

	public function 命宫() {
	}
}
