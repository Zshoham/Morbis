<template>
  <div class="my-10">
         <GameManage/>
  </div>
</template>

<script>
// @ is an alias to /src
import GameManage from '@/components/GameManage.vue'

export default {
  name: 'GameManagePage',
  components: {
    GameManage
  }
}
</script>