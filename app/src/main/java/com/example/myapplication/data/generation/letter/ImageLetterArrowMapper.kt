package com.example.myapplication.data.generation.letter

import com.example.myapplication.R

object ImageLetterArrowMapper {

    private val map = mapOf(

        "a_arrow" to R.drawable.a_arrow,
        "b_arrow" to R.drawable.b_arrow,
        "c_arrow" to R.drawable.c_arrow,
        "d_arrow" to R.drawable.d_arrow,
        "e_arrow" to R.drawable.e_arrow,
        "f_arrow" to R.drawable.f_arrow,
        "g_arrow" to R.drawable.g_arrow,
        "h_arrow" to R.drawable.h_arrow,
        "i_arrow" to R.drawable.i_arrow,
        "j_arrow" to R.drawable.j_arrow,
        "k_arrow" to R.drawable.k_arrow,
        "l_arrow" to R.drawable.l_arrow,
        "m_arrow" to R.drawable.m_arrow,
        "n_arrow" to R.drawable.n_arrow,
        "o_arrow" to R.drawable.o_arrow,
        "p_arrow" to R.drawable.p_arrow,
        "q_arrow" to R.drawable.q_arrow,
        "r_arrow" to R.drawable.r_arrow,
        "s_arrow" to R.drawable.s_arrow,
        "t_arrow" to R.drawable.t_arrow,
        "u_arrow" to R.drawable.u_arrow,
        "v_arrow" to R.drawable.v_arrow,
        "w_arrow" to R.drawable.w_arrow,
        "x_arrow" to R.drawable.x_arrow,
        "y_arrow" to R.drawable.y_arrow,
        "z_arrow" to R.drawable.z_arrow,

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