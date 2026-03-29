<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { FoodService, type Food } from './services/FoodService';

const foods = ref<Food[]>([]);
const pickedFood = ref<Food | null>(null);
const isPicking = ref(false);
const newFoodName = ref('');
const showList = ref(false);

const loadFoods = async () => {
  foods.value = await FoodService.getAll();
};

const pickMeal = async () => {
  if (foods.value.length === 0) {
    alert('请先添加一些食物！');
    return;
  }

  isPicking.value = true;
  pickedFood.value = null;

  let counter = 0;
  const totalSteps = 20;
  const intervalTime = 80;

  const timer = setInterval(() => {
    const randomIndex = Math.floor(Math.random() * foods.value.length);
    const tempFood = foods.value[randomIndex];

    if (tempFood) {
      pickedFood.value = tempFood;
    }

    counter++;

    if (counter >= totalSteps) {
      clearInterval(timer);
      finalizePick();
    }
  }, intervalTime);

  const finalizePick = async () => {
    try {
      const realResult = await FoodService.getRandom();
      if (realResult) {
        pickedFood.value = realResult;
      }
    } catch (error) {
      console.error("获取随机食物失败", error);
    } finally {
      isPicking.value = false;
    }
  };
};

const addFood = async () => {
  if (!newFoodName.value.trim()) {
    alert('请输入一些内容！');
    return;
  }
  
  try {
    await FoodService.add({ name: newFoodName.value });
    newFoodName.value = '';
    await loadFoods();
  } catch (error) {
    console.error('添加失败:', error);
    alert('添加失败，请检查后端服务是否正常运行，或查看控制台报错！');
  }
};

const deleteFood = async (id: number) => {
  await FoodService.delete(id);
  await loadFoods();
};

onMounted(loadFoods);
</script>

<template>
  <div class="glass-card animate-fade">
    <h1>🍴 今天打算吃什么？</h1>

    <div class="picker-section">
      <button @click="pickMeal" :disabled="isPicking" class="btn-primary">
        {{ isPicking ? '正在纠结中...' : '帮我选一个！' }}
      </button>

      <div
          v-if="pickedFood"
          class="result-card animate-bounce-in"
          :class="{ 'picking-animation': isPicking }"
      >
        <span class="emoji">😋</span>
        <h2>就吃这个了：{{ pickedFood.name }}</h2>
      </div>
    </div>

    <div class="divider"></div>

    <div class="manage-section">
      <div class="toggle-header" @click="showList = !showList">
        <span>管理我的菜单 ({{ foods.length }})</span>
        <span class="arrow">{{ showList ? '▼' : '▶' }}</span>
      </div>

      <div v-if="showList" class="list-container animate-fade">
        <div class="add-box">
          <input
              v-model="newFoodName"
              @keyup.enter="addFood"
              placeholder="输入新发现的美味..."
              type="text"
          />
          <button @click="addFood">添加</button>
        </div>

        <ul class="food-list">
          <li v-for="food in foods" :key="food.id">
            <span>{{ food.name }}</span>
            <button @click="food.id && deleteFood(food.id)" class="delete-btn">×</button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped>
.picker-section {
  text-align: center;
  min-height: 200px;
}

.result-card {
  margin-top: 30px;
  padding: 24px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.05);
  transition: all 0.1s ease-out; /* 增加平滑过渡 */
}

.result-card h2 {
  color: var(--primary);
  margin: 10px 0;
}

.emoji {
  font-size: 3rem;
}

.divider {
  height: 1px;
  background: var(--glass-border);
  margin: 40px 0 20px;
}

.manage-section {
  color: var(--text-muted);
}

.toggle-header {
  display: flex;
  justify-content: space-between;
  cursor: pointer;
  padding: 10px 0;
  font-weight: 500;
  transition: color 0.2s;
}

.toggle-header:hover {
  color: var(--primary);
}

.list-container {
  margin-top: 15px;
}

.add-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.add-box input {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ddd;
  outline: none;
}

.add-box button {
  padding: 0 20px;
  border-radius: 8px;
  border: none;
  background: var(--secondary);
  color: white;
  cursor: pointer;
}

.food-list {
  list-style: none;
  max-height: 200px;
  overflow-y: auto;
}

.food-list li {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.delete-btn {
  background: none;
  border: none;
  color: #ff8787;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0 5px;
}

.delete-btn:hover {
  color: #ff5252;
}

/* ✨ 修改点 2：去掉 opacity: 0.8 防止变暗，改用 brightness 增加亮度 */
.picking-animation {
  transform: scale(1.05);
  filter: brightness(1.1);
  box-shadow: 0 15px 30px rgba(255, 107, 107, 0.2);
}
</style>