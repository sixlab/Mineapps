package cn.sixlab.app.mineapps

import cn.sixlab.app.mineapps.ft.AppsFragment
import cn.sixlab.app.mineapps.ft.HomeFragment
import cn.sixlab.app.mineapps.ft.MineFragment


/**
 * Created by patrick on 2017/12/27.
 */
interface FragmentListener:HomeFragment.OnFragmentInteractionListener
        ,AppsFragment.OnFragmentInteractionListener
        ,MineFragment.OnFragmentInteractionListener