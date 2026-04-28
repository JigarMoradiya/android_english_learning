package com.example.myapplication.data.generation.letter

import com.example.myapplication.R

object ImageAlphabetMapper {

    private val map = mapOf(

        "a_outline_c" to R.drawable.a_outline_c,
        "b_outline_c" to R.drawable.b_outline_c,
        "c_outline_c" to R.drawable.c_outline_c,
        "d_outline_c" to R.drawable.d_outline_c,
        "e_outline_c" to R.drawable.e_outline_c,
        "f_outline_c" to R.drawable.f_outline_c,
        "g_outline_c" to R.drawable.g_outline_c,
        "h_outline_c" to R.drawable.h_outline_c,
        "i_outline_c" to R.drawable.i_outline_c,
        "j_outline_c" to R.drawable.j_outline_c,
        "k_outline_c" to R.drawable.k_outline_c,
        "l_outline_c" to R.drawable.l_outline_c,
        "m_outline_c" to R.drawable.m_outline_c,
        "n_outline_c" to R.drawable.n_outline_c,
        "o_outline_c" to R.drawable.o_outline_c,
        "p_outline_c" to R.drawable.p_outline_c,
        "q_outline_c" to R.drawable.q_outline_c,
        "r_outline_c" to R.drawable.r_outline_c,
        "s_outline_c" to R.drawable.s_outline_c,
        "t_outline_c" to R.drawable.t_outline_c,
        "u_outline_c" to R.drawable.u_outline_c,
        "v_outline_c" to R.drawable.v_outline_c,
        "w_outline_c" to R.drawable.w_outline_c,
        "x_outline_c" to R.drawable.x_outline_c,
        "y_outline_c" to R.drawable.y_outline_c,
        "z_outline_c" to R.drawable.z_outline_c,


    )

    fun get(name: String?): Int? {
        val key = name
            ?.trim()
            ?.lowercase()
            ?.replace("-", "_")
            ?.replace(" ", "_")
            ?: return null

        return map[key]
    }
}