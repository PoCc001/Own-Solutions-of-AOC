(set-logic QF_BV)
(declare-fun a () (_ BitVec 48))

(assert (= (_ bv2 48) (
    bvand (
        bvxor (bvlshr a (bvxor (bvand a (_ bv7 48)) (_ bv7 48))) a
    ) (_ bv7 48)
)))

(assert (= (_ bv4 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv3 48)) (bvxor (bvand (bvlshr a (_ bv3 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv3 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv1 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv6 48)) (bvxor (bvand (bvlshr a (_ bv6 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv6 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv7 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv9 48)) (bvxor (bvand (bvlshr a (_ bv9 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv9 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv7 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv12 48)) (bvxor (bvand (bvlshr a (_ bv12 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv12 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv5 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv15 48)) (bvxor (bvand (bvlshr a (_ bv15 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv15 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv1 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv18 48)) (bvxor (bvand (bvlshr a (_ bv18 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv18 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv7 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv21 48)) (bvxor (bvand (bvlshr a (_ bv21 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv21 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv4 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv24 48)) (bvxor (bvand (bvlshr a (_ bv24 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv24 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv6 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv27 48)) (bvxor (bvand (bvlshr a (_ bv27 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv27 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv0 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv30 48)) (bvxor (bvand (bvlshr a (_ bv30 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv30 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv3 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv33 48)) (bvxor (bvand (bvlshr a (_ bv33 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv33 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv5 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv36 48)) (bvxor (bvand (bvlshr a (_ bv36 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv36 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv5 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv39 48)) (bvxor (bvand (bvlshr a (_ bv39 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv39 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv3 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv42 48)) (bvxor (bvand (bvlshr a (_ bv42 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv42 48))
    ) (_ bv7 48)
)))

(assert (= (_ bv0 48) (
    bvand (
        bvxor (bvlshr (bvlshr a (_ bv45 48)) (bvxor (bvand (bvlshr a (_ bv45 48)) (_ bv7 48)) (_ bv7 48))) (bvlshr a (_ bv45 48))
    ) (_ bv7 48)
)))

(assert (bvugt (_ bv265061364605851 48) a))

(check-sat) (get-model) (exit)
