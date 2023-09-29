(ns day7
  (:require [clojure.string :as str]))

; Day 7 / #1 =====================================================================================================================

;; associates a key to a specific nested map, with key-order being a vec of all the keys
;; required to access the final map, which the key and value will be entered in.
(defn assoc-deep [map key-order key value]
  (loop [rem-path key-order level map levels []]
    (if (zero? (count rem-path))

      (let [lowest-map (assoc level key value)]
        (loop [reversed-levels (reverse levels)
               reversed-key-order (reverse key-order)
               building-map lowest-map]
          (if (zero? (count reversed-levels))
            building-map
            (recur
              (drop 1 reversed-levels)
              (drop 1 reversed-key-order)
              (assoc (first reversed-levels) (first reversed-key-order) building-map)))))

      (recur (drop 1 rem-path) (get level (first rem-path)) (conj levels level))
      )
    )
  )

(defn parse-file [line file-tree dir]
  (let [parts (str/split line #" ")
        arg (get parts 0)
        name (get parts 1)]
    (if (= arg "dir")
      (assoc-deep file-tree dir name {})
      (assoc-deep file-tree dir name (Integer/parseInt arg))
      )
    ))

(defn update-dir [arg dir]
  (case arg
    "/" ["/"]
    ".." (subvec dir 0 (dec (count dir)))
    (conj dir arg)))

(defn parse-command [line dir]
  (let [parts (str/split line #" ")
        command (get parts 1)]
    (case command
      "ls" dir
      "cd" (update-dir (get parts 2) dir))))

;; populates the file tree
(defn populate-file-tree [file-tree-list]
  (loop [rem-file-tree-list file-tree-list file-tree {} dir [] dir-list []]
    (if (zero? (count rem-file-tree-list))
      [file-tree (distinct dir-list)]
      (let [line (first rem-file-tree-list)]
        (if (str/starts-with? line "$")
          (recur (drop 1 rem-file-tree-list) file-tree (parse-command line dir) (conj dir-list dir))
          (recur (drop 1 rem-file-tree-list) (parse-file line file-tree dir) dir (conj dir-list dir)))))))

(def populated (populate-file-tree (str/split-lines "$ cd /\n$ ls\ndir cmvqf\ndir dcgbjvj\n57426 gszshjwr.lrs\ndir nsgms\n124423 rjqns.prb\ndir wqvv\n$ cd cmvqf\n$ ls\n6852 cnsb.cmm\n319810 cwqbmjb.vpl\ndir dcgbjvj\ndir ddnclwtd\ndir gccnrw\ndir qwzphd\ndir rvqwnjv\ndir ssmf\n107040 trttdw.jsn\ndir wcn\n296426 wqvv\n$ cd dcgbjvj\n$ ls\ndir dcgbjvj\ndir rlvcvj\n$ cd dcgbjvj\n$ ls\n214674 gsqcwfmz.hlm\n$ cd ..\n$ cd rlvcvj\n$ ls\n151752 cnsb.cmm\n256829 sjlwgf.mqn\n$ cd ..\n$ cd ..\n$ cd ddnclwtd\n$ ls\n177893 fpwznlp.zsf\n$ cd ..\n$ cd gccnrw\n$ ls\ndir mbfw\ndir rlvcvj\ndir wsrdh\ndir wvq\ndir zgpdl\n$ cd mbfw\n$ ls\ndir dcgbjvj\ndir mhnjvrl\n271166 ptrv\n$ cd dcgbjvj\n$ ls\ndir npjmq\n$ cd npjmq\n$ ls\n26712 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd mhnjvrl\n$ ls\n190094 mgrdrbl.lqg\n199191 zgczmvng\n22082 zgczmvng.rld\n$ cd ..\n$ cd ..\n$ cd rlvcvj\n$ ls\n244617 mbjprm\n264738 wpgglg\n$ cd ..\n$ cd wsrdh\n$ ls\ndir mgmp\n111558 vnqmnjpb.bnc\n$ cd mgmp\n$ ls\ndir whzjb\n$ cd whzjb\n$ ls\n235442 mgrdrbl.lqg\n63642 sphms.tzw\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd wvq\n$ ls\n23240 dcgbjvj.rwc\n79015 hcb\n155120 jjc\ndir wqvv\n207559 wqvv.cwp\n$ cd wqvv\n$ ls\n130961 cnsb.cmm\ndir fcl\n208524 hgbr.snf\ndir lzs\n14868 mgrdrbl.lqg\ndir sqpgtrn\n143653 zgczmvng\n$ cd fcl\n$ ls\ndir jfjgnz\n$ cd jfjgnz\n$ ls\n225416 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd lzs\n$ ls\n111949 vtcmf\n$ cd ..\n$ cd sqpgtrn\n$ ls\n289955 cnsb.cmm\ndir crstpjjv\ndir dcgbjvj\n6334 nwv.blw\ndir vpnhcsfr\n$ cd crstpjjv\n$ ls\n144739 jtcndb.wht\n16215 qdccst.dsg\n$ cd ..\n$ cd dcgbjvj\n$ ls\ndir bnzmws\ndir crstpjjv\n82495 fjmbgql\n248051 hlcwhnf\n145452 qwzjc.sth\ndir spmr\n268967 wqvv\n23371 wqvv.vdm\ndir zgczmvng\n$ cd bnzmws\n$ ls\ndir dcgbjvj\ndir dgbtqdn\n119626 pvgrqjf.ftq\n204879 tscrpt.szt\n$ cd dcgbjvj\n$ ls\n152171 msf.qhf\n$ cd ..\n$ cd dgbtqdn\n$ ls\n64965 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd crstpjjv\n$ ls\n97804 mgrdrbl.lqg\n88837 trttdw.jsn\n$ cd ..\n$ cd spmr\n$ ls\n302501 dcgbjvj\n$ cd ..\n$ cd zgczmvng\n$ ls\ndir crstpjjv\n187957 prznqbn\n$ cd crstpjjv\n$ ls\n218211 jlb.nvs\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd vpnhcsfr\n$ ls\n220411 qtcdjgz\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd zgpdl\n$ ls\ndir llstdpv\ndir rtftjm\n$ cd llstdpv\n$ ls\n318556 qqccwwjf.mbw\n$ cd ..\n$ cd rtftjm\n$ ls\n117705 fwphh.zrz\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd qwzphd\n$ ls\ndir crstpjjv\ndir fvfmlgql\ndir ldbts\ndir ljtcgz\ndir llvhbzpz\ndir plcbgmwc\ndir pwp\ndir qjstb\n58078 wmc\n$ cd crstpjjv\n$ ls\n171196 sbf.vcc\n320608 trttdw.jsn\n$ cd ..\n$ cd fvfmlgql\n$ ls\ndir hfdnml\n298497 trttdw.jsn\n$ cd hfdnml\n$ ls\n43441 crstpjjv.vrr\n$ cd ..\n$ cd ..\n$ cd ldbts\n$ ls\n211746 crstpjjv\n224627 rcw.rcl\n$ cd ..\n$ cd ljtcgz\n$ ls\ndir dfrnh\n179456 fmbpcdbd.vrl\n141254 fpwznlp.zsf\n86291 pcmqcl.jmz\n266763 pzvg.qcg\ndir zjjbjn\n$ cd dfrnh\n$ ls\ndir crstpjjv\n$ cd crstpjjv\n$ ls\n220983 wqvv.hhn\n$ cd ..\n$ cd ..\n$ cd zjjbjn\n$ ls\n215454 nwcbbv.mbb\n$ cd ..\n$ cd ..\n$ cd llvhbzpz\n$ ls\n206731 cnsb.cmm\n$ cd ..\n$ cd plcbgmwc\n$ ls\n223141 fpwznlp.zsf\ndir hplrsb\n309856 jhdwr.jfc\ndir mhmnmd\n218364 mmfzhj.zvg\ndir nwnj\n316432 trttdw.jsn\ndir vrgj\n$ cd hplrsb\n$ ls\ndir lbscwd\ndir lsffhj\ndir mlfp\ndir pqfbf\ndir tcvjzzhj\ndir wqvv\n$ cd lbscwd\n$ ls\n157261 wvblz.hmp\n$ cd ..\n$ cd lsffhj\n$ ls\n171621 crstpjjv\n$ cd ..\n$ cd mlfp\n$ ls\n80994 vvjzm.pzt\n$ cd ..\n$ cd pqfbf\n$ ls\n67861 ltd.zbw\ndir nnsg\ndir nwcl\n107828 rlvcvj\n160956 trttdw.jsn\n$ cd nnsg\n$ ls\n18252 tzcrqv.rsr\n$ cd ..\n$ cd nwcl\n$ ls\n38378 cnsb.cmm\n217283 dqwpwzz\n220081 mgrdrbl.lqg\n28106 sbf.vcc\n$ cd ..\n$ cd ..\n$ cd tcvjzzhj\n$ ls\n152965 dhv\n316034 gvtdrj.rft\n$ cd ..\n$ cd wqvv\n$ ls\n281962 mfzf.nfn\n95321 rlvcvj.zwf\n$ cd ..\n$ cd ..\n$ cd mhmnmd\n$ ls\n213145 cnsb.cmm\ndir hnzcz\n273060 mnwhg\ndir qcwdvq\n318596 trttdw.jsn\n$ cd hnzcz\n$ ls\n177795 fpwznlp.zsf\n188898 rlvcvj\n317234 wqvv.jsv\ndir zgczmvng\n$ cd zgczmvng\n$ ls\ndir qzfw\n$ cd qzfw\n$ ls\n134097 rlvcvj\n73145 sbf.vcc\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd qcwdvq\n$ ls\n83084 wqvv\n$ cd ..\n$ cd ..\n$ cd nwnj\n$ ls\n84366 hgpmqh\n317603 mgrdrbl.lqg\n$ cd ..\n$ cd vrgj\n$ ls\n136595 fpwznlp.zsf\n78517 sbf.vcc\ndir wqvv\n242465 wqvv.cpl\ndir zln\n$ cd wqvv\n$ ls\n3191 sbf.vcc\n$ cd ..\n$ cd zln\n$ ls\ndir crstpjjv\n$ cd crstpjjv\n$ ls\n86511 btqgw\n17597 rcstn.jpj\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd pwp\n$ ls\ndir crstpjjv\ndir jlqdbv\n290915 mgrdrbl.lqg\n219909 nfgj\n207313 sbf.vcc\ndir zpjf\n$ cd crstpjjv\n$ ls\n298992 crnfs.fgn\n172934 jqh\n$ cd ..\n$ cd jlqdbv\n$ ls\n300436 zgczmvng\n$ cd ..\n$ cd zpjf\n$ ls\n78904 sbf.vcc\n$ cd ..\n$ cd ..\n$ cd qjstb\n$ ls\ndir dtb\ndir gcd\ndir gmcnhh\n85552 htm.lzc\n219773 mzb.fvt\n208419 schz\n$ cd dtb\n$ ls\n167147 crstpjjv.zlb\n$ cd ..\n$ cd gcd\n$ ls\n239595 rlvcvj\n$ cd ..\n$ cd gmcnhh\n$ ls\n111653 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd rvqwnjv\n$ ls\n59215 cnsb.cmm\n37164 jtlcr.rlm\ndir mnc\n$ cd mnc\n$ ls\n32159 cnsb.cmm\n76204 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd ssmf\n$ ls\ndir fvcd\n127458 hpdzv\ndir jcg\n288242 jtjp.mjj\ndir jzp\n268857 mgrdrbl.lqg\n223968 nhfmbvc\ndir wqvv\n235806 wqvv.fnl\n$ cd fvcd\n$ ls\n26479 wcs.bdp\n$ cd ..\n$ cd jcg\n$ ls\ndir nsvtrs\ndir zgczmvng\n$ cd nsvtrs\n$ ls\ndir dcgbjvj\n$ cd dcgbjvj\n$ ls\n36633 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd zgczmvng\n$ ls\n221381 vszcg.jdb\n$ cd ..\n$ cd ..\n$ cd jzp\n$ ls\n1957 dcgbjvj\n$ cd ..\n$ cd wqvv\n$ ls\n9330 wqvv.pvs\n46963 ztlh\n$ cd ..\n$ cd ..\n$ cd wcn\n$ ls\ndir cqdzdnq\ndir cszzg\ndir fqmcr\n123361 pjfdtvzf.rdf\ndir rmrg\ndir rsfddzs\ndir vqrpdwv\ndir wpgddhdq\ndir wpgv\n$ cd cqdzdnq\n$ ls\ndir dqhpbsg\ndir qlq\ndir vfwhcpwl\ndir wqvv\ndir zpbbspcv\n$ cd dqhpbsg\n$ ls\n245289 glbfq.vpw\n51357 vsvvzbns.ftf\n$ cd ..\n$ cd qlq\n$ ls\n210318 mqgnjht.vqq\n$ cd ..\n$ cd vfwhcpwl\n$ ls\n109892 mmpzcjmp.znn\n$ cd ..\n$ cd wqvv\n$ ls\ndir chwdzfsg\ndir crstpjjv\ndir dcgbjvj\ndir rllbccjt\ndir rlvcvj\n$ cd chwdzfsg\n$ ls\n108951 fpwznlp.zsf\ndir vgc\n$ cd vgc\n$ ls\n273011 fpwznlp.zsf\n248078 ntc.ghp\n77305 thgbb.mfn\n73383 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd crstpjjv\n$ ls\ndir qmswb\n$ cd qmswb\n$ ls\n68252 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd dcgbjvj\n$ ls\n111 qtcs.llc\ndir szzthsmj\n$ cd szzthsmj\n$ ls\ndir qhztdv\ndir wqvv\ndir zmqrftlm\n$ cd qhztdv\n$ ls\n138433 qrdsrrb.chw\ndir rlvcvj\n$ cd rlvcvj\n$ ls\ndir jfcm\ndir lzf\n249984 rlvcvj.nmb\ndir twrs\n$ cd jfcm\n$ ls\n117884 dtrc.wsm\n237577 rlvcvj.mhd\n$ cd ..\n$ cd lzf\n$ ls\n60342 nrc.clh\n$ cd ..\n$ cd twrs\n$ ls\n97201 cnsb.cmm\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd wqvv\n$ ls\n73047 gcqzjf.gcb\n$ cd ..\n$ cd zmqrftlm\n$ ls\ndir ltd\n$ cd ltd\n$ ls\n120673 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd rllbccjt\n$ ls\n299611 cnsb.cmm\ndir lnfvlqh\n36418 qwh\ndir rlvcvj\n255907 trttdw.jsn\ndir zqvzpv\n$ cd lnfvlqh\n$ ls\n195010 fpwznlp.zsf\n72496 llrznf.rwc\ndir lvgzb\n53126 mgrdrbl.lqg\n90191 mnrqtn\n310156 nthdm.crh\n$ cd lvgzb\n$ ls\n209837 mgrdrbl.lqg\n210074 rwhgmd\n70338 sbf.vcc\n$ cd ..\n$ cd ..\n$ cd rlvcvj\n$ ls\n252080 dcgbjvj\n$ cd ..\n$ cd zqvzpv\n$ ls\n249229 zpt.lbc\n$ cd ..\n$ cd ..\n$ cd rlvcvj\n$ ls\ndir dcgbjvj\ndir fdz\n$ cd dcgbjvj\n$ ls\n191363 trttdw.jsn\n$ cd ..\n$ cd fdz\n$ ls\n291107 bqsdfc.rcn\ndir dcgbjvj\n64333 fpwznlp.zsf\ndir lfb\n280608 mgrdrbl.lqg\n125554 trttdw.jsn\n$ cd dcgbjvj\n$ ls\n169326 qwjhpdh\n$ cd ..\n$ cd lfb\n$ ls\n35299 zqfnjtr.clt\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd zpbbspcv\n$ ls\n54549 mgrdrbl.lqg\n$ cd ..\n$ cd ..\n$ cd cszzg\n$ ls\ndir wqvv\n$ cd wqvv\n$ ls\n136042 crstpjjv.jtq\n10879 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd fqmcr\n$ ls\n188798 bchvt.dvw\n276819 fpwznlp.zsf\ndir gdr\ndir rlvcvj\n5623 zgczmvng.fqs\n158621 znddbv\n$ cd gdr\n$ ls\ndir btg\n213096 cnsb.cmm\ndir dhbcmzbz\n$ cd btg\n$ ls\ndir hjm\n$ cd hjm\n$ ls\n144774 zgczmvng.llz\n$ cd ..\n$ cd ..\n$ cd dhbcmzbz\n$ ls\n148108 rlvcvj\n$ cd ..\n$ cd ..\n$ cd rlvcvj\n$ ls\n201103 qrdlf.pvg\n272776 vnpgw.wts\n153826 zgczmvng\n290248 zgczmvng.gsv\n$ cd ..\n$ cd ..\n$ cd rmrg\n$ ls\ndir jzb\ndir nsslsw\ndir rlvcvj\n$ cd jzb\n$ ls\n273968 trttdw.jsn\n$ cd ..\n$ cd nsslsw\n$ ls\n226370 sbf.vcc\n$ cd ..\n$ cd rlvcvj\n$ ls\n294706 gsbqswjj\n$ cd ..\n$ cd ..\n$ cd rsfddzs\n$ ls\ndir cphvtp\n205384 crstpjjv\n82103 dfrjwrnz.bfl\ndir fntvngpm\n297145 pqtrvd\n237572 sbf.vcc\ndir zgczmvng\n$ cd cphvtp\n$ ls\ndir phvc\n$ cd phvc\n$ ls\n52239 dcgbjvj.lbj\n$ cd ..\n$ cd ..\n$ cd fntvngpm\n$ ls\n18297 mgrdrbl.lqg\n$ cd ..\n$ cd zgczmvng\n$ ls\ndir lzcwf\ndir pqmc\n179956 tzqjcn\ndir zgczmvng\n$ cd lzcwf\n$ ls\n284166 cnsb.cmm\n157214 jhmmn.qwn\n$ cd ..\n$ cd pqmc\n$ ls\n215883 nlvdqw.jmt\ndir qjfr\n209722 wqvv.fgg\n$ cd qjfr\n$ ls\n53013 mgrdrbl.lqg\n191236 sgmnjc\n$ cd ..\n$ cd ..\n$ cd zgczmvng\n$ ls\n260649 dglqpjs\n141213 mgrdrbl.lqg\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd vqrpdwv\n$ ls\ndir ftw\n150895 mgrdrbl.lqg\n227641 nbrzfl.dpf\ndir nwjdnpdd\n$ cd ftw\n$ ls\n99672 dbsgvvbp\ndir dnzld\n146730 mgrdrbl.lqg\n$ cd dnzld\n$ ls\n37598 bhjbfl.svw\ndir qspsslt\n$ cd qspsslt\n$ ls\ndir vhgpwvf\n$ cd vhgpwvf\n$ ls\n146936 ghgl\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd nwjdnpdd\n$ ls\n46000 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd wpgddhdq\n$ ls\ndir rqgf\n$ cd rqgf\n$ ls\n197374 rlvcvj.fmr\n$ cd ..\n$ cd ..\n$ cd wpgv\n$ ls\ndir fdh\n286086 fpwznlp.zsf\ndir pljq\n258062 wqvv\ndir zgczmvng\n$ cd fdh\n$ ls\n76173 fpwznlp.zsf\n230947 nczhtpcn\n62630 rlvcvj\n$ cd ..\n$ cd pljq\n$ ls\ndir mzmm\n41117 rjms.dcg\n$ cd mzmm\n$ ls\n144202 zgczmvng.ttl\n$ cd ..\n$ cd ..\n$ cd zgczmvng\n$ ls\ndir dncr\ndir mcdmfdp\ndir pgqglmj\ndir qldrmn\n$ cd dncr\n$ ls\n198052 dcgbjvj\ndir dqdgft\ndir hpmwvnsr\n2829 rlvcvj.qwg\n$ cd dqdgft\n$ ls\ndir fng\ndir nlsmb\n$ cd fng\n$ ls\n198899 zgczmvng\n$ cd ..\n$ cd nlsmb\n$ ls\n257121 gmr.vmg\n9276 zsmd.bng\n$ cd ..\n$ cd ..\n$ cd hpmwvnsr\n$ ls\n241101 jjwqbwl.fpl\n64151 wqvv\n196139 zgczmvng\n$ cd ..\n$ cd ..\n$ cd mcdmfdp\n$ ls\n276856 lrgbhq\n$ cd ..\n$ cd pgqglmj\n$ ls\n36476 fpwznlp.zsf\n$ cd ..\n$ cd qldrmn\n$ ls\n295686 trttdw.jsn\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd dcgbjvj\n$ ls\ndir brdfvd\ndir crstpjjv\ndir fzdqcgv\ndir fzw\ndir mrllpw\ndir wnh\n119561 zgczmvng.jsm\n$ cd brdfvd\n$ ls\n272932 mhdjc.mng\ndir wqvv\n91053 zgczmvng.jwg\n$ cd wqvv\n$ ls\ndir bpplph\n121367 jhlqfn.sbs\n61760 nsgbt\n46653 sbf.vcc\n16952 trttdw.jsn\n$ cd bpplph\n$ ls\n1288 rlvcvj\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd crstpjjv\n$ ls\n117722 sbf.vcc\n$ cd ..\n$ cd fzdqcgv\n$ ls\n289819 fpwznlp.zsf\n$ cd ..\n$ cd fzw\n$ ls\ndir dcgbjvj\ndir zsfqwdth\ndir zswdl\n$ cd dcgbjvj\n$ ls\n272427 cnsb.cmm\n$ cd ..\n$ cd zsfqwdth\n$ ls\ndir bdbgtqjj\ndir hcgrqbhl\n$ cd bdbgtqjj\n$ ls\n318980 tnqmspdf.cwd\n$ cd ..\n$ cd hcgrqbhl\n$ ls\n135307 fpwznlp.zsf\ndir wqvv\n$ cd wqvv\n$ ls\n68708 ctz.wms\n149578 wlvdrfsw.qcj\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd zswdl\n$ ls\n259754 cnsb.cmm\n$ cd ..\n$ cd ..\n$ cd mrllpw\n$ ls\n44007 tvsm\n$ cd ..\n$ cd wnh\n$ ls\ndir mjnrmb\n156515 wpdhq.hvp\n$ cd mjnrmb\n$ ls\n293592 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd nsgms\n$ ls\ndir dnhzj\ndir ptc\ndir tnfrr\ndir vjt\n32152 zgczmvng.wmt\n$ cd dnhzj\n$ ls\n281195 flqbvrw.gmf\n177042 jjsfrmc.drz\n$ cd ..\n$ cd ptc\n$ ls\ndir gjvnrcln\n290797 pccmrnn\n59802 rzl.tjm\ndir zgczmvng\n$ cd gjvnrcln\n$ ls\ndir jph\n$ cd jph\n$ ls\n105648 hflqlwr.mph\n158151 hmlqsp\n$ cd ..\n$ cd ..\n$ cd zgczmvng\n$ ls\n3700 bwn.wqq\n240004 jbvhs.chq\n224969 mvftsj\n$ cd ..\n$ cd ..\n$ cd tnfrr\n$ ls\ndir hlbrpt\n237956 mgrdrbl.lqg\ndir wqvv\n$ cd hlbrpt\n$ ls\n34424 crstpjjv.rlw\ndir dzs\n275267 mwrvw\n313095 nwqzrc.tnf\n61808 wzhgm.fft\n$ cd dzs\n$ ls\n274302 cbbvq.vvh\n234166 dcgbjvj.cbq\n253156 fpwznlp.zsf\n7239 nzdbr\n$ cd ..\n$ cd ..\n$ cd wqvv\n$ ls\n120918 hchsfcp.clm\n105770 nfhrd.tts\n$ cd ..\n$ cd ..\n$ cd vjt\n$ ls\ndir crstpjjv\n$ cd crstpjjv\n$ ls\n81225 fpwznlp.zsf\n$ cd ..\n$ cd ..\n$ cd ..\n$ cd wqvv\n$ ls\n72054 bcld.nwh\n284293 cvsmmh\n32684 ndgnz\n130836 rlvcvj\n233437 sbf.vcc\ndir szwnlv\n$ cd szwnlv\n$ ls\n133507 bnmhmpr.vww")))

(defn get-folder-size [file-tree path]
  (let [target-file-tree (get-in file-tree path)]
    (reduce +
      (for [item (keys target-file-tree)
          :let [value (get target-file-tree item)]]
        (if (int? value)
          value
          (get-folder-size value []))))))                    ; path is null because this is already the target folder

(defn get-folder-sizes [file-tree dirs]
  (map #(get-folder-size file-tree %) dirs)
  )

(defn get-answer [file-tree dirs]
  (reduce + (filter #(<= % 100000) (get-folder-sizes file-tree dirs))))

(println (get-answer (get populated 0) (get populated 1)))

; Day 7 / #2 =====================================================================================================================

(defn get-folder-size-of-deletion [file-tree dirs]
  (let [unused-size (- 70000000 (get-folder-size (get populated 0) []))
        to-be-deleted-size (- 30000000 unused-size)
        folder-sizes (get-folder-sizes file-tree dirs)]
    (apply min (filter #(>= % to-be-deleted-size) folder-sizes))))

(println (get-folder-size-of-deletion (get populated 0) (get populated 1)))

