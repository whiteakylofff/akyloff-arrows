package me.whiteakyloff.arrows.arrow;

import lombok.Getter;
import lombok.AllArgsConstructor;

import org.bukkit.inventory.ItemStack;

import me.whiteakyloff.arrows.arrow.abilities.data.CustomArrowAbility;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class CustomArrow
{
    private String name;

    private UUID UUID;

    private ItemStack itemStack;

    private HashMap<String, String> arrowData;

    private List<CustomArrowAbility> arrowAbilities;
}
